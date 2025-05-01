package com.ventuit.adminstrativeapp.core.mappers.interfaces;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.ventuit.adminstrativeapp.core.models.BaseModel;

public interface CrudMapperInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ENTITY extends BaseModel> {

    ENTITY toEntity(CREATINGDTO dto);

    @Named("toDto")
    CREATINGDTO toDto(ENTITY entity);

    @IterableMapping(qualifiedByName = "toDto")
    List<CREATINGDTO> entitiesToDtos(List<ENTITY> listEntities);

    @Named("toShowDto")
    LISTDTO toShowDto(ENTITY entity);

    @IterableMapping(qualifiedByName = "toShowDto")
    List<LISTDTO> entitiesToShowDtos(List<ENTITY> listEntities);

    ENTITY updateFromDto(UPDATINGDTO dto, @MappingTarget ENTITY entity);
}
