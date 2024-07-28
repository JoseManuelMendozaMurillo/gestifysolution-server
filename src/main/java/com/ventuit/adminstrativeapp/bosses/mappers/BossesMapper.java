package com.ventuit.adminstrativeapp.bosses.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

import com.ventuit.adminstrativeapp.bosses.dto.BossesDto;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BossesMapper implements CrudMapperInterface<BossesDto, BossesModel> {

    @Override
    public abstract List<BossesDto> entitiesToDtos(List<BossesModel> listEntities);

    @Override
    public abstract BossesDto toDto(BossesModel entity);

    @Override
    public abstract BossesModel toEntity(BossesDto dto);

    @Override
    public abstract BossesModel updateFromDto(BossesDto dto, @MappingTarget BossesModel entity);

}
