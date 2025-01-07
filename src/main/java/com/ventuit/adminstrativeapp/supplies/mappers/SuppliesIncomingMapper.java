package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesIncomingDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesIncomingDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesIncomingModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SuppliesIncomingMapper implements
                CrudMapperInterface<CreateSuppliesIncomingDto, UpdateSuppliesIncomingDto, CreateSuppliesIncomingDto, SuppliesIncomingModel> {

        @Override
        @Mapping(target = "id", ignore = true)
        public abstract SuppliesIncomingModel toEntity(CreateSuppliesIncomingDto dto);

        @Override
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "deletedAt", ignore = true)
        @Mapping(target = "deletedBy", ignore = true)
        @Mapping(target = "updatedBy", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        public abstract SuppliesIncomingModel updateFromDto(UpdateSuppliesIncomingDto dto,
                        @MappingTarget SuppliesIncomingModel entity);

}
