package com.ventuit.adminstrativeapp.businesses.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.ventuit.adminstrativeapp.businesses.dto.BusinessesDto;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BusinessesMapper
        implements CrudMapperInterface<BusinessesDto, BusinessesDto, BusinessesDto, BusinessesModel> {

    @Override
    @Named("toDto")
    public abstract BusinessesDto toDto(BusinessesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<BusinessesDto> entitiesToDtos(List<BusinessesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract BusinessesDto toShowDto(BusinessesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<BusinessesDto> entitiesToShowDtos(List<BusinessesModel> listEntities);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BusinessesModel toEntity(BusinessesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract BusinessesModel updateFromDto(BusinessesDto dto, @MappingTarget BusinessesModel entity);
}
