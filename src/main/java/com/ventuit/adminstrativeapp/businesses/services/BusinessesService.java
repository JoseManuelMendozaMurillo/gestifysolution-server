package com.ventuit.adminstrativeapp.businesses.services;

import java.io.IOException;
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
import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;
import com.ventuit.adminstrativeapp.shared.services.FilesService;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class BusinessesService
        extends
        CrudServiceImpl<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, BusinessesModel, Integer, BusinessesMapper, BusinessesRepository> {

    @Autowired
    private FilesService filesService;
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

}
