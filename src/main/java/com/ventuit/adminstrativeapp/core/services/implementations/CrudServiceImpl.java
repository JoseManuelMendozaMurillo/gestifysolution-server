package com.ventuit.adminstrativeapp.core.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.core.models.BaseModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;

public abstract class CrudServiceImpl<DTO, ENTITY extends BaseModel, ID, MAPPER extends CrudMapperInterface<DTO, ENTITY>, REPOSITORY extends BaseRepository<ENTITY, ID>>
        implements CrudServiceInterface<DTO, ID> {

    protected REPOSITORY repository;
    protected MAPPER mapper;

    public CrudServiceImpl(REPOSITORY repository, MAPPER mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DTO> getAll() {
        return this.mapper.entitiesToDtos(this.repository.findByDeletedAtIsNull());
    }

    @Override
    public DTO getById(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findByIdAndDeletedAtIsNull(id);

        if (!optionalEntity.isPresent())
            return null;

        return this.mapper.toDto(optionalEntity.get());
    }

    @SuppressWarnings("unused")
    @Override
    public ResponseEntity<?> create(DTO dto) {
        ENTITY entity = this.mapper.toEntity(dto);
        ENTITY savedEntity = this.repository.save(entity);
        if (savedEntity == null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok("The record was created successfully");
    }

    @Override
    public Boolean update(ID id, DTO dto) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return false;

        ENTITY existingEntity = optionalEntity.get();
        ENTITY updatedEntity = this.mapper.updateFromDto(dto, existingEntity);
        return updatedEntity != null; // Return true if the entity was updated successfully
    }

    @Override
    public Boolean delete(ID id) {
        Optional<ENTITY> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Deleting the entity
        this.repository.softRemoveById(id);

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

}
