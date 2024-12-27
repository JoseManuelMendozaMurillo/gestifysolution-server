package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SuppliersMapper
        implements CrudMapperInterface<CreateSuppliersDto, UpdateSuppliersDto, CreateSuppliersDto, SuppliersModel> {

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract SuppliersModel toEntity(CreateSuppliersDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "direction", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activeChangedAt", ignore = true)
    @Mapping(target = "activeChangedBy", ignore = true)
    public abstract SuppliersModel updateFromDto(UpdateSuppliersDto dto, @MappingTarget SuppliersModel entity);

}
