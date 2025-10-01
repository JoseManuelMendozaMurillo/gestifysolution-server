package com.ventuit.adminstrativeapp.products.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;
import com.ventuit.adminstrativeapp.products.mappers.ProductsImagesMapper;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsImagesRepository;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.products.services.interfaces.ProductsImagesServiceInterfaces;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;
import com.ventuit.adminstrativeapp.shared.helpers.AuthenticationHelper;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.shared.validators.ObjectsValidator;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;
import com.ventuit.adminstrativeapp.storage.minio.services.MinioService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsImagesService implements ProductsImagesServiceInterfaces {

    private final EntityManager entityManager;

    private final FilesServiceImpl filesService;
    private final MinioProvider minioProvider;
    private final MinioService minioService;
    private final ObjectsValidator<CreateProductsImagesDto> creatingDtoValidator;
    private final ProductsImagesRepository repository;
    private final ProductsRepository productsRepository;
    private final ProductsImagesMapper mapper;
    private final AuthenticationHelper authHelper;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductsImagesDto create(CreateProductsImagesDto createDto) {
        creatingDtoValidator.validate(createDto);

        // If the new image is a portrait, set all other images for this product to be
        // non-portrait
        if (Boolean.TRUE.equals(createDto.getPortrait())) {
            repository.setPortraitToFalseForProduct(createDto.getProductId());
        }

        ProductsImagesModel productImage = mapper.toEntity(createDto);
        FilesModel fileModel = null;

        try {
            MultipartFile image = createDto.getImage();

            // The MinIO client needs to read the stream to calculate a signature and then
            // rewind it to send the actual data. We must wrap it in a BufferedInputStream
            // and mark the stream's beginning with a read limit large enough for the
            // entire file.
            BufferedInputStream imageStream = new BufferedInputStream(image.getInputStream());
            imageStream.mark((int) image.getSize() + 1);

            // Use streams to avoid loading the whole file into memory
            FileUploadDto fileUploadDto = FileUploadDto.builder()
                    .fileInputStream(imageStream)
                    .fileSize(image.getSize())
                    .contentType(image.getContentType())
                    .originalFileName(image.getOriginalFilename())
                    .path("products/images")
                    .provider("minio")
                    .bucket(minioProvider.getBucketName())
                    .build();

            fileModel = filesService.uploadFile(fileUploadDto);
            productImage.setFile(fileModel);

            ProductsImagesModel productImageSaved = repository.save(productImage);
            entityManager.refresh(productImageSaved);

            return mapper.toShowDto(productImageSaved);

        } catch (IOException e) {
            // If there's an IO error (e.g., reading the stream), delete the orphaned file
            // if it was created
            minioService.deleteFile(fileModel);
            throw new FileUploadException("Error processing image file for product.", e);
        } catch (Exception e) {
            // For any other exception, the transaction will roll back.
            // If the file was uploaded, we must manually delete it.
            minioService.deleteFile(fileModel);
            throw e;
        }
    }

    @Override
    public ListProductsImagesDto getById(Integer id) {
        Optional<ProductsImagesModel> optionalProductImage = this.repository.findById(id);

        if (!optionalProductImage.isPresent()) {
            return null;
        }

        return this.mapper.toShowDto(optionalProductImage.get());
    }

    @Override
    public Boolean softDeleteById(Integer id) {
        Optional<ProductsImagesModel> optionalProductImage = this.repository.findById(id);

        if (!optionalProductImage.isPresent())
            return null; // Not found

        // Deleting the entity
        String username = this.authHelper.getUsername();
        this.repository.softDeleteById(id, username);

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    public Boolean deleteById(Integer id) {
        Optional<ProductsImagesModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return null;

        ProductsImagesModel productsImages = optionalEntity.get();
        FilesModel fileToDelete = productsImages.getFile();

        // Deleting the entity
        productsImages.getProduct().getImages().remove(productsImages);
        this.repository.deleteById(id);

        // 3. If the database deletion was successful, delete the physical file from
        // MinIO
        if (fileToDelete != null) {
            this.minioService.deleteFile(fileToDelete);
        }

        // Checking if the entity was deleted
        return !this.repository.findById(id).isPresent();
    }

    @Override
    public Boolean restoreById(Integer id) {
        Optional<ProductsImagesModel> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return null;

        // Restoring the entity
        this.repository.restoreById(id);

        // Checking if the entity was restored
        return this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    public List<ListProductsImagesDto> getByProductId(Integer id) {
        Optional<ProductsModel> optionalProduct = this.productsRepository.findById(id);

        if (!optionalProduct.isPresent()) {
            return null;
        }

        List<ProductsImagesModel> images = this.repository.findByProductId(id);

        if (images.isEmpty()) {
            return List.of();
        }

        return this.mapper.entitiesToShowDtos(images);
    }

    @Override
    public List<ListProductsImagesDto> getActiveByProductId(Integer id) {
        Optional<ProductsModel> optionalProduct = this.productsRepository.findById(id);

        if (!optionalProduct.isPresent()) {
            return null;
        }

        List<ProductsImagesModel> images = this.repository.findByProductIdAndDeletedAtIsNull(id);

        if (images.isEmpty()) {
            return List.of();
        }

        return this.mapper.entitiesToShowDtos(images);
    }

    @Override
    public List<ListProductsImagesDto> getInactiveByProductId(Integer id) {
        Optional<ProductsModel> optionalProduct = this.productsRepository.findById(id);

        if (!optionalProduct.isPresent()) {
            return null;
        }

        List<ProductsImagesModel> images = this.repository.findByProductIdAndDeletedAtIsNotNull(id);

        if (images.isEmpty()) {
            return List.of();
        }

        return this.mapper.entitiesToShowDtos(images);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductsImagesDto setAsPortraitImage(Integer id) {
        Optional<ProductsImagesModel> optionalProductImage = this.repository.findById(id);

        if (!optionalProductImage.isPresent()) {
            return null; // Not found
        }

        ProductsImagesModel productImage = optionalProductImage.get();
        Integer productId = productImage.getProduct().getId();

        // Set all other images for this product to be non-portrait
        repository.setPortraitToFalseForProduct(productId);

        // Set this image as portrait
        productImage.setPortrait(true);
        ProductsImagesModel updatedImage = repository.save(productImage);

        return mapper.toShowDto(updatedImage);
    }
}
