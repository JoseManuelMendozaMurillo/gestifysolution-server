package com.ventuit.adminstrativeapp.products.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ventuit.adminstrativeapp.products.dto.ProductInterestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.CreateProductImageDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.mappers.ProductsMapper;
import com.ventuit.adminstrativeapp.branches.models.BranchesProductsModel;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsImagesRepository;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.products.services.interfaces.ProductsServiceInterface;
import com.ventuit.adminstrativeapp.products.specifications.ProductsSpecification;
import com.ventuit.adminstrativeapp.products.dto.ProductsSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;
import com.ventuit.adminstrativeapp.storage.minio.services.MinioService;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ProductsService extends
        CrudServiceImpl<CreateProductDto, UpdateProductDto, ListProductDto, ProductsModel, Integer, ProductsMapper, ProductsRepository>
        implements ProductsServiceInterface {

    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;
    @Autowired
    private MinioService minioService;
    @Autowired
    private ProductsImagesRepository productsImagesRepository;
    @Autowired
    private BranchesRepository branchesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductsService(ProductsRepository repository, ProductsMapper mapper) {
        super(repository, mapper);
    }

    public Page<ListProductDto> getAll(
            Pageable pageable,
            Integer categoryId) {
        return this.repository.findAll(categoryId, pageable).map(this.mapper::toShowDto);
    }

    public Page<ListProductDto> getAllActive(
            Pageable pageable, Integer categoryId) {
        return this.repository.findAllActive(categoryId, pageable).map(this.mapper::toShowDto);
    }

    public Page<ListProductDto> getAllInactive(
            Pageable pageable, Integer categoryId) {
        return this.repository.findAllInactive(categoryId, pageable).map(this.mapper::toShowDto);
    }

    public Page<ListProductDto> search(ProductsSearchCriteria criteria, Pageable pageable) {
        Specification<ProductsModel> spec = ProductsSpecification.searchByCriteria(criteria);
        return this.repository.findAll(spec, pageable).map(this.mapper::toShowDto);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductDto create(CreateProductDto createDto) {
        creatingDtoValidator.validate(createDto);

        List<FilesModel> uploadedFiles = new ArrayList<>();
        ProductsModel product = mapper.toEntity(createDto);
        try {
            // Upload images
            for (CreateProductImageDto image : createDto.getImages()) {
                FileUploadDto fileUploadDto = FileUploadDto.builder()
                        .fileInputStream(new ByteArrayInputStream(image.getImage().getBytes()))
                        .fileSize((long) image.getImage().getBytes().length)
                        .contentType(image.getImage().getContentType())
                        .originalFileName(image.getImage().getOriginalFilename())
                        .path("products/images")
                        .provider("minio")
                        .bucket(minioProvider.getBucketName())
                        .build();
                FilesModel productImage = filesService.uploadFile(fileUploadDto);
                uploadedFiles.add(productImage);

                ProductsImagesModel productsImagesModel = ProductsImagesModel.builder()
                        .file(productImage)
                        .product(product)
                        .portrait(image.getPortrait())
                        .build();
                product.getImages().add(productsImagesModel);
            }

            // Associate branches
            for (Integer branchId : createDto.getBranchIds()) {
                BranchesProductsModel branchProduct = BranchesProductsModel.builder()
                        .product(product)
                        .branch(branchesRepository.findById(branchId).orElseThrow())
                        .build();
                product.getBranchesProducts().add(branchProduct);
            }

            // Save the product
            ProductsModel productSaved = repository.save(product);
            entityManager.refresh(productSaved);

            return mapper.toShowDto(productSaved);
        } catch (IOException e) {
            deleteUploadedFiles(uploadedFiles);
            throw new RuntimeException("Error uploading product images", e);
        } catch (Exception e) {
            deleteUploadedFiles(uploadedFiles);
            throw e;
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductDto update(Integer id, UpdateProductDto updateDto) {
        updatingDtoValidator.validate(updateDto);

        Optional<ProductsModel> optionalProduct = this.repository.findById(id);

        if (!optionalProduct.isPresent())
            return null;

        ProductsModel existingProduct = optionalProduct.get();

        ProductsModel updatedProduct = this.mapper.updateFromDto(updateDto, existingProduct);

        // If no images are provided, just save the updated product without new images
        if (updateDto.getImages() == null || updateDto.getImages().isEmpty()) {
            updatedProduct = repository.saveAndFlush(updatedProduct);

            // Update branch associations if provided
            if (updateDto.getBranchIds() != null) {
                updateBranchAssociations(updatedProduct, updateDto.getBranchIds());
                updatedProduct = repository.saveAndFlush(updatedProduct);
            }

            return mapper.toShowDto(updatedProduct);
        }

        // if images are provided, handle the image upload
        List<FilesModel> uploadedFiles = new ArrayList<>();
        try {
            // Check if any new image has portrait=true
            boolean hasNewPortraitImage = updateDto.getImages().stream()
                    .anyMatch(CreateProductImageDto::getPortrait);

            // If there's a new portrait image, set all existing images' portrait to false
            if (hasNewPortraitImage) {
                updatedProduct.getImages().forEach(img -> img.setPortrait(false));
                updatedProduct = repository.saveAndFlush(updatedProduct);
            }

            // Upload images
            for (CreateProductImageDto image : updateDto.getImages()) {
                FileUploadDto fileUploadDto = FileUploadDto.builder()
                        .fileInputStream(new ByteArrayInputStream(image.getImage().getBytes()))
                        .fileSize((long) image.getImage().getBytes().length)
                        .contentType(image.getImage().getContentType())
                        .originalFileName(image.getImage().getOriginalFilename())
                        .path("products/images")
                        .provider("minio")
                        .bucket(minioProvider.getBucketName())
                        .build();
                FilesModel productImage = filesService.uploadFile(fileUploadDto);
                uploadedFiles.add(productImage);

                ProductsImagesModel productsImagesModel = ProductsImagesModel.builder()
                        .file(productImage)
                        .product(updatedProduct)
                        .portrait(image.getPortrait())
                        .build();
                updatedProduct.getImages().add(productsImagesModel);
            }

            // Update the product
            updatedProduct = this.repository.saveAndFlush(updatedProduct);

            // Update branch associations if provided
            if (updateDto.getBranchIds() != null) {
                updateBranchAssociations(updatedProduct, updateDto.getBranchIds());
                updatedProduct = repository.saveAndFlush(updatedProduct);
            }

            return mapper.toShowDto(updatedProduct);
        } catch (IOException e) {
            deleteUploadedFiles(uploadedFiles);
            throw new RuntimeException("Error uploading product images", e);
        } catch (Exception e) {
            deleteUploadedFiles(uploadedFiles);
            throw e;
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        Optional<ProductsModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Soft Deleting the files images
        Set<ProductsImagesModel> images = optionalEntity.get().getImages();
        images.forEach(img -> {
            filesService.softDeleteFileFromAllBuckets(img.getFile().getId(), getUsername());
            this.productsImagesRepository.softDeleteById(img.getId(), getUsername());
        });

        // Deleting the entity
        this.repository.softDeleteById(id, this.getUsername());

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        Optional<ProductsModel> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return true;

        // Restore images
        Set<ProductsImagesModel> images = optionalEntity.get().getImages();
        images.forEach(img -> {
            filesService.restoreFileFromAllBuckets(img.getFile().getId());
            this.productsImagesRepository.restoreById(img.getId());
        });

        // Restoring the entity
        this.repository.restoreById(id);

        // Checking if the entity was restored
        return this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean deleteById(Integer id) {
        Optional<ProductsModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        ProductsModel product = optionalEntity.get();

        List<ProductsImagesModel> imagesToDelete = new ArrayList<>(product.getImages());
        product.getImages().clear();

        imagesToDelete.forEach(img -> {
            productsImagesRepository.deleteById(img.getId());
            filesService.deleteFileFromAllBuckets(img.getFile().getId());
        });

        this.repository.delete(product);

        return !this.repository.findById(id).isPresent();
    }

    @Override
    public Page<ListProductDto> searchProducts(ProductsSearchCriteria criteria, Pageable pageable) {
        return this.repository.findAll(ProductsSpecification.searchByCriteria(criteria), pageable)
                .map(this.mapper::toShowDto);
    }

    @Override
    public Optional<ProductInterestDto> getProductInterest(Integer productId) {
        Optional<ProductsModel> optionalProduct = this.repository.findByIdAndDeletedAtIsNull(productId);

        if (!optionalProduct.isPresent()) {
            return Optional.empty();
        }

        ProductsModel product = optionalProduct.get();

        List<Integer> branchIds = product.getBranchesProducts().stream()
                .map(bp -> bp.getBranch().getId())
                .collect(java.util.stream.Collectors.toList());

        String message = String.format(
                "Hola, estoy interesado en comprar el producto %s. ¿Me podrían dar más información sobre disponibilidad y precio? ¡Gracias!",
                product.getName());

        return Optional.of(ProductInterestDto.builder()
                .message(message)
                .branchIds(branchIds)
                .build());
    }

    private void deleteUploadedFiles(List<FilesModel> files) {
        for (FilesModel file : files) {
            String productImagePath = file.getFilesPaths().getPath() + "/" + file.getFileKey();
            minioService.deleteFile(productImagePath);
        }
    }

    private void updateBranchAssociations(ProductsModel product, List<Integer> branchIds) {
        // Clear existing associations
        product.getBranchesProducts().clear();

        // Add new associations
        for (Integer branchId : branchIds) {
            BranchesProductsModel branchProduct = BranchesProductsModel.builder()
                    .product(product)
                    .branch(branchesRepository.findById(branchId).orElseThrow())
                    .build();
            product.getBranchesProducts().add(branchProduct);
        }
    }
}
