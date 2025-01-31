package com.ventuit.adminstrativeapp.core.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;
import com.ventuit.adminstrativeapp.shared.validators.ObjectsValidator;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CrudServiceImpl<CREATINGDTO extends ExtendedBaseDto, UPDATINGDTO extends ExtendedBaseDto, LISTDTO extends ExtendedBaseDto, ENTITY extends ExtendedBaseModel, ID, MAPPER extends CrudMapperInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ENTITY>, REPOSITORY extends BaseRepository<ENTITY, ID>>
        implements CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    protected REPOSITORY repository;
    protected MAPPER mapper;
    protected ObjectsValidator<CREATINGDTO> creatingDtoValidator = new ObjectsValidator<CREATINGDTO>();
    protected ObjectsValidator<UPDATINGDTO> updatingDtoValidator = new ObjectsValidator<UPDATINGDTO>();

    public CrudServiceImpl(REPOSITORY repository, MAPPER mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<LISTDTO> getAll() {
        return this.mapper.entitiesToShowDtos(this.repository.findAll());
    }

    @Override
    public List<LISTDTO> getAllInactive() {
        return this.mapper.entitiesToShowDtos(this.repository.findByDeletedAtIsNotNull());
    }

    @Override
    public List<LISTDTO> getAllActive() {
        return this.mapper.entitiesToShowDtos(this.repository.findByDeletedAtIsNull());
    }

    @Override
    public LISTDTO getById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return null;

        return this.mapper.toShowDto(optionalEntity.get());
    }

    @Override
    public LISTDTO getByActiveId(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findByIdAndDeletedAtIsNull(id);

        if (!optionalEntity.isPresent())
            return null;

        return this.mapper.toShowDto(optionalEntity.get());
    }

    @Override
    public LISTDTO getByInactiveId(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return null;

        return this.mapper.toShowDto(optionalEntity.get());
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void create(CREATINGDTO dto) {
        // Validate the dto
        this.creatingDtoValidator.validate(dto);

        ENTITY entity = this.mapper.toEntity(dto);

        entity.setCreatedBy(this.getUsername());

        this.repository.save(entity);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean update(ID id, UPDATINGDTO dto) {
        // Validate the dto
        this.updatingDtoValidator.validate(dto);

        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return false;

        ENTITY existingEntity = optionalEntity.get();

        dto.setUpdatedBy(this.getUsername());

        ENTITY updatedEntity = this.mapper.updateFromDto(dto, existingEntity);

        return updatedEntity != null; // Return true if the entity was updated successfully
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Deleting the entity
        this.repository.softDeleteById(id, this.getUsername());

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean deleteById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Deleting the entity
        this.repository.deleteById(id);

        // Checking if the entity was deleted
        return !this.repository.findById(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return true;

        // Restoring the entity
        this.repository.restoreById(id);

        // Checking if the entity was restored
        return this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    protected Jwt getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt;
        } else
            return null;
    }

    protected String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            return principal.getAttribute("preferred_username");
        }
        return "Gestify solution server"; // or a default value
    }

}
