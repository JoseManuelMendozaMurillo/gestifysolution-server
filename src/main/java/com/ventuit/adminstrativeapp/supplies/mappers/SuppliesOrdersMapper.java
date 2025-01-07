package com.ventuit.adminstrativeapp.supplies.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesOrdersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesOrdersDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesOrdersModel;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SuppliesOrdersMapper implements
        CrudMapperInterface<CreateSuppliesOrdersDto, UpdateSuppliesOrdersDto, CreateSuppliesOrdersDto, SuppliesOrdersModel> {

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract SuppliesOrdersModel toEntity(CreateSuppliesOrdersDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract SuppliesOrdersModel updateFromDto(UpdateSuppliesOrdersDto dto,
            @MappingTarget SuppliesOrdersModel entity);

}