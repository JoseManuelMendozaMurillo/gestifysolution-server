package com.ventuit.adminstrativeapp.products.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.CreateProductImageDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.mappers.ProductsMapper;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsImagesRepository;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
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
        CrudServiceImpl<CreateProductDto, UpdateProductDto, ListProductDto, ProductsModel, Integer, ProductsMapper, ProductsRepository> {

    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;
    @Autowired
    private MinioService minioService;
    @Autowired
    private ProductsImagesRepository productsImagesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductsService(ProductsRepository repository, ProductsMapper mapper) {
        super(repository, mapper);
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

    private void deleteUploadedFiles(List<FilesModel> files) {
        for (FilesModel file : files) {
            String productImagePath = file.getFilesPaths().getPath() + "/" + file.getFileKey();
            minioService.deleteFile(productImagePath);
        }
    }
}
