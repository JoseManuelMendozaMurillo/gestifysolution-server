package com.ventuit.adminstrativeapp.products.services;

import java.io.IOException;
import java.util.Optional;
import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.mappers.ProductsCategoriesMapper;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ProductsCategoriesService extends
        CrudServiceImpl<CreateProductsCategoryDto, UpdateProductsCategoryDto, ListProductsCategoryDto, ProductsCategoriesModel, Integer, ProductsCategoriesMapper, ProductsCategoriesRepository> {

    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;

    public ProductsCategoriesService(ProductsCategoriesRepository repository, ProductsCategoriesMapper mapper) {
        super(repository, mapper);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductsCategoryDto create(CreateProductsCategoryDto createDto) {
        this.creatingDtoValidator.validate(createDto);

        MultipartFile image = createDto.getImage();
        FilesModel filesModel = null;
        try {
            byte[] fileBytes = image.getBytes();
            FileUploadDto fileUploadDto = new FileUploadDto();
            fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
            fileUploadDto.setFileSize((long) fileBytes.length);
            fileUploadDto.setContentType(image.getContentType());
            fileUploadDto.setOriginalFileName(image.getOriginalFilename());
            fileUploadDto.setPath("products/categories/images");
            fileUploadDto.setProvider("minio");
            fileUploadDto.setBucket(minioProvider.getBucketName());
            filesModel = filesService.uploadFile(fileUploadDto);
        } catch (IOException e) {
            throw new FileUploadException("Error uploading image", e);
        }

        ProductsCategoriesModel entity = mapper.toEntity(createDto);
        entity.setImage(filesModel);
        ProductsCategoriesModel entitySaved = repository.save(entity);
        entityManager.refresh(entitySaved);
        return mapper.toShowDto(entitySaved);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductsCategoryDto update(Integer id, UpdateProductsCategoryDto update) {
        // Validate the dto
        this.updatingDtoValidator.validate(update);

        // Find the existing entity
        ProductsCategoriesModel entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product category not found with id: " + id));

        // Handle image update if present
        MultipartFile image = update.getImage();
        FilesModel filesModel = null;
        if (image != null) {
            try {
                byte[] fileBytes = image.getBytes();
                FileUploadDto fileUploadDto = new FileUploadDto();
                fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
                fileUploadDto.setFileSize((long) fileBytes.length);
                fileUploadDto.setContentType(image.getContentType());
                fileUploadDto.setOriginalFileName(image.getOriginalFilename());
                fileUploadDto.setPath("products/categories/images");
                fileUploadDto.setProvider("minio");
                fileUploadDto.setBucket(minioProvider.getBucketName());
                filesModel = filesService.updateFile(entity.getImage().getId(), fileUploadDto);
            } catch (IOException e) {
                throw new FileUploadException("Error uploading file", e);
            }
        }

        // Map the update DTO to the entity (except image)
        ProductsCategoriesModel updatedEntity = mapper.updateFromDto(update, entity);

        // Set the image if updated
        if (filesModel != null) {
            updatedEntity.setImage(filesModel);
        }

        ProductsCategoriesModel entitySaved = repository.saveAndFlush(updatedEntity);

        return mapper.toShowDto(entitySaved);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        // Find the existing entity
        Optional<ProductsCategoriesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        ProductsCategoriesModel entity = optionalEntity.get();

        // Delete the image
        filesService.softDeleteFileFromAllBuckets(entity.getImage().getId(), getUsername());

        // Delete the entity
        repository.softDeleteById(id, getUsername());

        // Return true if the entity was deleted
        return !repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        // Find the existing entity
        Optional<ProductsCategoriesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        ProductsCategoriesModel entity = optionalEntity.get();

        // Restore the image
        filesService.restoreFileFromAllBuckets(entity.getImage().getId());

        // Restore the entity
        repository.restoreById(id);

        // Return true if the entity was restored
        return repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean deleteById(Integer id) {
        // Find the existing entity
        Optional<ProductsCategoriesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        ProductsCategoriesModel entity = optionalEntity.get();

        // Delete the entity
        repository.deleteById(id);

        // Delete the image file
        System.out.println("Deleting image");
        filesService.deleteFileFromAllBuckets(entity.getImage().getId());

        // Return true if the entity was deleted
        return !repository.findById(id).isPresent();
    }

}
