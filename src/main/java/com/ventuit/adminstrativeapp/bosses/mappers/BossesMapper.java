package com.ventuit.adminstrativeapp.bosses.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.ventuit.adminstrativeapp.bosses.dto.BossesDto;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BossesMapper implements CrudMapperInterface<BossesDto, BossesDto, BossesDto, BossesModel> {

    @Override
    @Named("toDto")
    public abstract BossesDto toDto(BossesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<BossesDto> entitiesToDtos(List<BossesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract BossesDto toShowDto(BossesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<BossesDto> entitiesToShowDtos(List<BossesModel> listEntities);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BossesModel toEntity(BossesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract BossesModel updateFromDto(BossesDto dto, @MappingTarget BossesModel entity);

}
