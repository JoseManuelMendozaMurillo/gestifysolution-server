package com.ventuit.adminstrativeapp.core.mappers.interfaces;

import java.util.List;

import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.core.models.BaseModel;

public interface CrudMapperInterface<CREATINGDTO extends BaseDto, UPDATINGDTO extends BaseDto, LISTDTO extends BaseDto, ENTITY extends BaseModel> {

    ENTITY toEntity(CREATINGDTO dto);

    CREATINGDTO toDto(ENTITY entity);

    List<CREATINGDTO> entitiesToDtos(List<ENTITY> listEntities);

    List<LISTDTO> entitiesToShowDtos(List<ENTITY> listEntities);

    LISTDTO toShowDto(ENTITY entity);

    ENTITY updateFromDto(UPDATINGDTO dto, @MappingTarget ENTITY entity);
}
