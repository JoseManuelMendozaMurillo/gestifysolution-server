package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesMeasureDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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
        @Mapping(target = "createdBy", ignore = true)
        public abstract SuppliesMeasureModel updateFromDto(CreateSuppliesMeasureDto dto,
                        @MappingTarget SuppliesMeasureModel entity);

}
