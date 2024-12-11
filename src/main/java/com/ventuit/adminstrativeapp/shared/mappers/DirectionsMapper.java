package com.ventuit.adminstrativeapp.shared.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class DirectionsMapper implements CrudMapperInterface<DirectionsDto, DirectionsModel> {
    @Override
    public abstract List<DirectionsDto> entitiesToDtos(List<DirectionsModel> listEntities);

    @Override
    public abstract DirectionsDto toDto(DirectionsModel entity);

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract DirectionsModel toEntity(DirectionsDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract DirectionsModel updateFromDto(DirectionsDto dto, @MappingTarget DirectionsModel entity);
}
