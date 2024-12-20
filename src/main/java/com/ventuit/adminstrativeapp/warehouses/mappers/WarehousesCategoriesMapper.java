package com.ventuit.adminstrativeapp.warehouses.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class WarehousesCategoriesMapper implements
                CrudMapperInterface<CreateWarehousesCategoriesDto, UpdateWarehousesCategoriesDto, CreateWarehousesCategoriesDto, WarehousesCategoriesModel> {

        @Override
        @Mapping(target = "id", ignore = true)
        public abstract WarehousesCategoriesModel toEntity(CreateWarehousesCategoriesDto dto);

        @Override
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "deletedAt", ignore = true)
        @Mapping(target = "deletedBy", ignore = true)
        @Mapping(target = "updatedBy", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        public abstract WarehousesCategoriesModel updateFromDto(UpdateWarehousesCategoriesDto dto,
                        @MappingTarget WarehousesCategoriesModel entity);

}
