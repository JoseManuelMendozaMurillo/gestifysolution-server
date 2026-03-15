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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.products.mappers.ProductsMapper;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.businesses.dto.BusinessesSearchCriteria;
import com.ventuit.adminstrativeapp.businesses.specifications.BusinessesSpecification;
import org.springframework.data.jpa.domain.Specification;

@Service
public class BusinessesService
        extends
        CrudServiceImpl<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, BusinessesModel, Integer, BusinessesMapper, BusinessesRepository> 
        implements BusinessesServiceInterface {

    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsMapper productsMapper;

    public BusinessesService(BusinessesRepository repository, BusinessesMapper mapper) {
        super(repository, mapper);
    }

    @Transactional(value = TxType.SUPPORTS)
    public Page<ListProductDto> getProductsByBusinessId(Integer businessId, Integer categoryId, Pageable pageable) {
        return productsRepository.findDistinctProductsByBusinessId(businessId, categoryId, pageable)
                .map(productsMapper::toShowDto);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListBusinessesDto create(CreateBusinessesDto createBusinessesDto) {
        // Validate the dto
        this.creatingDtoValidator.validate(createBusinessesDto);

        // Create the file for the logo
        MultipartFile logo = createBusinessesDto.getLogo();
        FilesModel logoModel = uploadFile(logo, "businesses/logos");

        // Create the file for the cover
        MultipartFile cover = createBusinessesDto.getCover();
        FilesModel coverModel = uploadFile(cover, "businesses/covers");

        BusinessesModel entity = mapper.toEntity(createBusinessesDto);

        entity.setLogo(logoModel);
        entity.setCover(coverModel);

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
        FilesModel logoModel = handleFileUpload(logo, entity.getLogo(), "businesses/logos");

        // Handle cover update if present
        MultipartFile cover = update.getCover();
        FilesModel coverModel = handleFileUpload(cover, entity.getCover(), "businesses/covers");

        // Map the update DTO to the entity (except logo and cover)
        BusinessesModel updatedEntity = mapper.updateFromDto(update, entity);

        // Set the logo if updated
        if (logoModel != null) {
            updatedEntity.setLogo(logoModel);
        }

        // Set the cover if updated
        if (coverModel != null) {
            updatedEntity.setCover(coverModel);
        }

        BusinessesModel entitySaved = repository.saveAndFlush(updatedEntity);

        return mapper.toShowDto(entitySaved);
    }

    private FilesModel uploadFile(MultipartFile file, String path) {
        if (file == null) {
            return null;
        }
        try {
            byte[] fileBytes = file.getBytes();

            FileUploadDto fileUploadDto = new FileUploadDto();
            fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
            fileUploadDto.setFileSize((long) fileBytes.length);
            fileUploadDto.setContentType(file.getContentType());
            fileUploadDto.setOriginalFileName(file.getOriginalFilename());
            fileUploadDto.setPath(path);
            fileUploadDto.setProvider("minio");
            fileUploadDto.setBucket(minioProvider.getBucketName());
            return filesService.uploadFile(fileUploadDto);
        } catch (IOException e) {
            throw new FileUploadException("Error uploading file", e);
        }
    }

    private FilesModel handleFileUpload(MultipartFile file, FilesModel existingFile, String path) {
        if (file == null) {
            return null;
        }
        try {
            byte[] fileBytes = file.getBytes();

            FileUploadDto fileUploadDto = new FileUploadDto();
            fileUploadDto.setFileInputStream(new ByteArrayInputStream(fileBytes));
            fileUploadDto.setFileSize((long) fileBytes.length);
            fileUploadDto.setContentType(file.getContentType());
            fileUploadDto.setOriginalFileName(file.getOriginalFilename());
            fileUploadDto.setPath(path);
            fileUploadDto.setProvider("minio");
            fileUploadDto.setBucket(minioProvider.getBucketName());

            if (existingFile != null) {
                return filesService.updateFile(existingFile.getId(), fileUploadDto);
            } else {
                return filesService.uploadFile(fileUploadDto);
            }
        } catch (IOException e) {
            throw new FileUploadException("Error uploading file", e);
        }
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

        // Delete the cover if it exists
        if (entity.getCover() != null) {
            filesService.softDeleteFileFromAllBuckets(entity.getCover().getId(), getUsername());
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

        // Restore the cover if it exists
        if (entity.getCover() != null) {
            filesService.restoreFileFromAllBuckets(entity.getCover().getId());
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

        // Remove the logo and cover references and save
        Integer logoId = null;
        if (entity.getLogo() != null) {
            logoId = entity.getLogo().getId();
            entity.setLogo(null);
        }

        Integer coverId = null;
        if (entity.getCover() != null) {
            coverId = entity.getCover().getId();
            entity.setCover(null);
        }

        if (logoId != null || coverId != null) {
            repository.saveAndFlush(entity); // Save the entity without the references
            entityManager.flush();
        }

        // Delete the logo file if it existed
        if (logoId != null) {
            filesService.deleteFileFromAllBuckets(logoId);
        }

        // Delete the cover file if it existed
        if (coverId != null) {
            filesService.deleteFileFromAllBuckets(coverId);
        }

        // Delete the entity
        repository.deleteById(id);

        // Return true if the entity was deleted
        return !repository.findById(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.SUPPORTS)
    public Page<ListBusinessesDto> searchBusinesses(BusinessesSearchCriteria criteria, Pageable pageable) {
        Specification<BusinessesModel> spec = BusinessesSpecification.searchByCriteria(criteria);
        Page<BusinessesModel> businesses = repository.findAll(spec, pageable);
        return businesses.map(mapper::toShowDto);
    }
}
