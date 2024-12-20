package com.ventuit.adminstrativeapp.warehouses.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class WarehousesMapper implements
        CrudMapperInterface<CreateWarehousesDto, UpdateWarehousesDto, CreateWarehousesDto, WarehousesModel> {

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract WarehousesModel toEntity(CreateWarehousesDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activeChangedAt", ignore = true)
    @Mapping(target = "activeChangedBy", ignore = true)
    public abstract WarehousesModel updateFromDto(UpdateWarehousesDto dto, @MappingTarget WarehousesModel entity);

}
