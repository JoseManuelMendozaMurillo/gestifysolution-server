package com.ventuit.adminstrativeapp.businesses.services;

import java.io.IOException;
import java.util.Optional;
import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.mappers.BusinessesMapper;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;
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
public class BusinessesService
        extends
        CrudServiceImpl<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, BusinessesModel, Integer, BusinessesMapper, BusinessesRepository> {

    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;

    public BusinessesService(BusinessesRepository repository, BusinessesMapper mapper) {
        super(repository, mapper);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListBusinessesDto create(CreateBusinessesDto createBusinessesDto) {
        // Validate the dto
        this.creatingDtoValidator.validate(createBusinessesDto);

        // Create the file for the logo
        MultipartFile logo = createBusinessesDto.getLogo();
        FilesModel filesModel = null;
        if (logo != null) {
            try {
                byte[] fileBytes = logo.getBytes(); // Read once

                FileUploadDto fileUploadDto = new FileUploadDto();
                fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
                fileUploadDto.setFileSize((long) fileBytes.length);
                fileUploadDto.setContentType(logo.getContentType());
                fileUploadDto.setOriginalFileName(logo.getOriginalFilename());
                fileUploadDto.setPath("businesses/logos");
                fileUploadDto.setProvider("minio");
                fileUploadDto.setBucket(minioProvider.getBucketName());
                filesModel = filesService.uploadFile(fileUploadDto);
            } catch (IOException e) {
                throw new FileUploadException("Error uploading file", e);
            }
        }

        BusinessesModel entity = mapper.toEntity(createBusinessesDto);

        entity.setLogo(filesModel);

        BusinessesModel entitySaved = repository.save(entity);

        entityManager.refresh(entitySaved);

        return mapper.toShowDto(entitySaved);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListBusinessesDto update(Integer id, UpdateBusinessesDto update) {
        // Validate the dto
        this.updatingDtoValidator.validate(update);

        // Find the existing entity
        BusinessesModel entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found with id: " + id));

        // Handle logo update if present
        MultipartFile logo = update.getLogo();
        FilesModel filesModel = null;
        if (logo != null) {
            try {
                byte[] fileBytes = logo.getBytes();

                FileUploadDto fileUploadDto = new FileUploadDto();
                fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
                fileUploadDto.setFileSize((long) fileBytes.length);
                fileUploadDto.setContentType(logo.getContentType());
                fileUploadDto.setOriginalFileName(logo.getOriginalFilename());
                fileUploadDto.setPath("businesses/logos");
                fileUploadDto.setProvider("minio");
                fileUploadDto.setBucket(minioProvider.getBucketName());

                // If there is an existing logo, update it, otherwise upload new
                if (entity.getLogo() != null) {
                    filesModel = filesService.updateFile(entity.getLogo().getId(), fileUploadDto);
                } else {
                    filesModel = filesService.uploadFile(fileUploadDto);
                }
            } catch (IOException e) {
                throw new FileUploadException("Error uploading file", e);
            }
        }

        // Map the update DTO to the entity (except logo)
        BusinessesModel updatedEntity = mapper.updateFromDto(update, entity);

        // Set the logo if updated
        if (filesModel != null) {
            updatedEntity.setLogo(filesModel);
        }

        BusinessesModel entitySaved = repository.saveAndFlush(updatedEntity);

        return mapper.toShowDto(entitySaved);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        // Find the existing entity
        Optional<BusinessesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        BusinessesModel entity = optionalEntity.get();

        // Delete the logo if it exists
        if (entity.getLogo() != null) {
            filesService.softDeleteFileFromAllBuckets(entity.getLogo().getId(), getUsername());
        }

        // Delete the entity
        repository.softDeleteById(id, getUsername());

        // Return true if the entity was deleted
        return !repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        // Find the existing entity
        Optional<BusinessesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        BusinessesModel entity = optionalEntity.get();

        // Restore the logo if it exists
        if (entity.getLogo() != null) {
            filesService.restoreFileFromAllBuckets(entity.getLogo().getId());
        }

        // Restore the entity
        repository.restoreById(id);

        // Return true if the entity was restored
        return repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean deleteById(Integer id) {
        // Find the existing entity
        Optional<BusinessesModel> optionalEntity = repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        BusinessesModel entity = optionalEntity.get();

        // Remove the logo reference and save
        Integer logoId = null;
        if (entity.getLogo() != null) {
            logoId = entity.getLogo().getId();
            entity.setLogo(null);
            repository.saveAndFlush(entity); // Save the entity without the logo reference
            entityManager.flush(); // If you have access to the entity manager
        }

        // Delete the logo file if it existed
        if (logoId != null) {
            System.out.println("Deleting logo");
            filesService.deleteFileFromAllBuckets(logoId);
        } else {
            System.out.println("No logo to delete");
        }

        // Delete the entity
        repository.deleteById(id);

        // Return true if the entity was deleted
        return !repository.findById(id).isPresent();
    }
}
