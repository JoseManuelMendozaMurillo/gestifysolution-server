package com.ventuit.adminstrativeapp.core.mappers.interfaces;

import java.util.List;

import org.mapstruct.MappingTarget;

public interface CrudMapperInterface<DTO, ENTITY> {

    ENTITY toEntity(DTO dto);

    DTO toDto(ENTITY entity);

    List<DTO> entitiesToDtos(List<ENTITY> listEntities);

    ENTITY updateFromDto(DTO dto, @MappingTarget ENTITY entity);
}
