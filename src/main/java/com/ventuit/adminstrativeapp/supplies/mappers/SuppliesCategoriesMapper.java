package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesCategoriesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesCategoriesDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SuppliesCategoriesMapper implements
                CrudMapperInterface<CreateSuppliesCategoriesDto, UpdateSuppliesCategoriesDto, CreateSuppliesCategoriesDto, SuppliesCategoriesModel> {

        @Override
        @Mapping(target = "id", ignore = true)
        public abstract SuppliesCategoriesModel toEntity(CreateSuppliesCategoriesDto dto);

        @Override
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "deletedAt", ignore = true)
        @Mapping(target = "deletedBy", ignore = true)
        @Mapping(target = "updatedBy", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        public abstract SuppliesCategoriesModel updateFromDto(UpdateSuppliesCategoriesDto dto,
                        @MappingTarget SuppliesCategoriesModel entity);

}
