package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.ListSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SuppliesMapper
        implements CrudMapperInterface<CreateSuppliesDto, UpdateSuppliesDto, ListSuppliesDto, SuppliesModel> {

    @Override
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activeChangedAt", ignore = true)
    @Mapping(target = "activeChangedBy", ignore = true)
    @Mapping(target = "suppliers", ignore = true)
    public abstract SuppliesModel toEntity(CreateSuppliesDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activeChangedAt", ignore = true)
    @Mapping(target = "activeChangedBy", ignore = true)
    @Mapping(target = "suppliers", ignore = true)
    public abstract SuppliesModel updateFromDto(UpdateSuppliesDto dto, @MappingTarget SuppliesModel entity);

}
