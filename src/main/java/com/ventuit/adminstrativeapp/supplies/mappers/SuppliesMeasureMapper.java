package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesMeasureDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;

public abstract class SuppliesMeasureMapper
        implements
        CrudMapperInterface<CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, SuppliesMeasureModel> {

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract SuppliesMeasureModel toEntity(CreateSuppliesMeasureDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract SuppliesMeasureModel updateFromDto(CreateSuppliesMeasureDto dto,
            @MappingTarget SuppliesMeasureModel entity);

}
