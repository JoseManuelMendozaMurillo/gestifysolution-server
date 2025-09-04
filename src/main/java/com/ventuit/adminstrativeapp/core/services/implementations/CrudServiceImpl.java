package com.ventuit.adminstrativeapp.core.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;
import com.ventuit.adminstrativeapp.shared.helpers.AuthenticationHelper;
import com.ventuit.adminstrativeapp.shared.validators.ObjectsValidator;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CrudServiceImpl<CREATINGDTO, UPDATINGDTO, LISTDTO, ENTITY extends ExtendedBaseModel, ID, MAPPER extends CrudMapperInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ENTITY>, REPOSITORY extends BaseRepository<ENTITY, ID>>
        implements CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    protected REPOSITORY repository;
    protected MAPPER mapper;

    @Autowired
    protected ObjectsValidator<CREATINGDTO> creatingDtoValidator;
    @Autowired
    protected ObjectsValidator<UPDATINGDTO> updatingDtoValidator;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected AuthenticationHelper authHelper;

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
    public LISTDTO create(CREATINGDTO dto) {
        // Validate the dto
        this.creatingDtoValidator.validate(dto);

        ENTITY entity = this.mapper.toEntity(dto);

        ENTITY entitySaved = this.repository.save(entity);

        entityManager.refresh(entitySaved);

        return this.mapper.toShowDto(entitySaved);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public LISTDTO update(ID id, UPDATINGDTO dto) {
        // Validate the dto
        this.updatingDtoValidator.validate(dto);

        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return null;

        ENTITY existingEntity = optionalEntity.get();

        ENTITY updatedEntity = this.mapper.updateFromDto(dto, existingEntity);

        updatedEntity = this.repository.saveAndFlush(updatedEntity);

        return this.mapper.toShowDto(updatedEntity); // Return true if the entity was updated successfully
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
        return authHelper.getUsername();
    }

}
