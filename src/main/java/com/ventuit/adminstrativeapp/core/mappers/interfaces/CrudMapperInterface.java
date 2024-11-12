package com.ventuit.adminstrativeapp.core.mappers.interfaces;

import java.util.List;

import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.core.models.BaseModel;

public interface CrudMapperInterface<DTO extends BaseDto, ENTITY extends BaseModel> {

    ENTITY toEntity(DTO dto);

    DTO toDto(ENTITY entity);

    List<DTO> entitiesToDtos(List<ENTITY> listEntities);

    ENTITY updateFromDto(DTO dto, @MappingTarget ENTITY entity);
}
