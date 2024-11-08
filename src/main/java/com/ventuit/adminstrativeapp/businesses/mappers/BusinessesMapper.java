package com.ventuit.adminstrativeapp.businesses.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.businesses.dto.BusinessesDto;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BusinessesMapper implements CrudMapperInterface<BusinessesDto, BusinessesModel> {
    @Override
    public abstract List<BusinessesDto> entitiesToDtos(List<BusinessesModel> listEntities);

    @Override
    public abstract BusinessesDto toDto(BusinessesModel entity);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BusinessesModel toEntity(BusinessesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BusinessesModel updateFromDto(BusinessesDto dto, @MappingTarget BusinessesModel entity);
}
