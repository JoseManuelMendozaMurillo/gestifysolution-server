package com.ventuit.adminstrativeapp.bosses.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BossesMapper
        implements CrudMapperInterface<CreateBossesDto, UpdateBossesDto, CreateBossesDto, BossesModel> {

    @Override
    @Named("toDto")
    public abstract CreateBossesDto toDto(BossesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateBossesDto> entitiesToDtos(List<BossesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract CreateBossesDto toShowDto(BossesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<CreateBossesDto> entitiesToShowDtos(List<BossesModel> listEntities);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BossesModel toEntity(CreateBossesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "keycloakUserId", ignore = true) // Add this line
    public abstract BossesModel updateFromDto(UpdateBossesDto dto, @MappingTarget BossesModel entity);

}
