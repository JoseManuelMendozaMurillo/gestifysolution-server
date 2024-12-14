package com.ventuit.adminstrativeapp.businesses.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BusinessesMapper
        implements CrudMapperInterface<CreateBusinessesDto, UpdateBusinessesDto, CreateBusinessesDto, BusinessesModel> {

    @Override
    @Named("toDto")
    public abstract CreateBusinessesDto toDto(BusinessesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateBusinessesDto> entitiesToDtos(List<BusinessesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract CreateBusinessesDto toShowDto(BusinessesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<CreateBusinessesDto> entitiesToShowDtos(List<BusinessesModel> listEntities);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BusinessesModel toEntity(CreateBusinessesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
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
    public abstract BusinessesModel updateFromDto(UpdateBusinessesDto dto, @MappingTarget BusinessesModel entity);
}
