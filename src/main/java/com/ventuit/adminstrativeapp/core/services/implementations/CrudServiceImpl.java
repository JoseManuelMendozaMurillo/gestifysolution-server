package com.ventuit.adminstrativeapp.core.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.core.models.BaseModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;

public abstract class CrudServiceImpl<CREATINGDTO extends BaseDto, UPDATINGDTO extends BaseDto, LISTDTO extends BaseDto, ENTITY extends BaseModel, ID, MAPPER extends CrudMapperInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ENTITY>, REPOSITORY extends BaseRepository<ENTITY, ID>>
        implements CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    protected REPOSITORY repository;
    protected MAPPER mapper;

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

    @SuppressWarnings("unused")
    @Override
    public ResponseEntity<?> create(CREATINGDTO dto) {
        ENTITY entity = this.mapper.toEntity(dto);
        ENTITY savedEntity = this.repository.save(entity);
        if (savedEntity == null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok("The record was created successfully");
    }

    @Override
    public Boolean update(ID id, UPDATINGDTO dto) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return false;

        ENTITY existingEntity = optionalEntity.get();

        dto.setDeletedAt(existingEntity.getDeletedAt()); // preserve logical deletion when updating

        ENTITY updatedEntity = this.mapper.updateFromDto(dto, existingEntity);

        return updatedEntity != null; // Return true if the entity was updated successfully
    }

    @Override
    public Boolean softDeleteById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Deleting the entity
        this.repository.softDeleteById(id);

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
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
    public Boolean restoreById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return true;

        // Restoring the entity
        this.repository.restoreById(id);

        // Checking if the entity was restored
        return this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

}
