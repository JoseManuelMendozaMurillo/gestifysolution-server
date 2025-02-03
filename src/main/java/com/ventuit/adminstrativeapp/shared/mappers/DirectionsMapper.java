package com.ventuit.adminstrativeapp.shared.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class DirectionsMapper
        implements CrudMapperInterface<DirectionsDto, DirectionsDto, DirectionsDto, DirectionsModel> {

    @Override
    @Named("toDto")
    public abstract DirectionsDto toDto(DirectionsModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<DirectionsDto> entitiesToDtos(List<DirectionsModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract List<DirectionsDto> entitiesToShowDtos(List<DirectionsModel> listEntities);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract DirectionsDto toShowDto(DirectionsModel entity);

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract DirectionsModel toEntity(DirectionsDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract DirectionsModel updateFromDto(DirectionsDto dto, @MappingTarget DirectionsModel entity);
}
