package com.ventuit.adminstrativeapp.bosses.mappers;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.ListBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.keycloak.dto.ListKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.mappers.KeycloakUserMapper;
import com.ventuit.adminstrativeapp.keycloak.services.implementations.KeycloakUsersServiceImpl;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BossesMapper
        implements CrudMapperInterface<CreateBossesDto, UpdateBossesDto, ListBossesDto, BossesModel> {

    @Autowired
    private KeycloakUsersServiceImpl keycloakUsersService;
    @Autowired
    private KeycloakUserMapper keycloakUserMapper;

    @Override
    @Named("toDto")
    @Mapping(target = "user", ignore = true)
    public abstract CreateBossesDto toDto(BossesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateBossesDto> entitiesToDtos(List<BossesModel> listEntities);

    @Override
    @Named("toShowDto")
    @Mapping(target = "user", expression = "java(getKeycloakUser(entity.getKeycloakUsername()))")
    public abstract ListBossesDto toShowDto(BossesModel entity);

    @Named("getKeycloakUser")
    public ListKeycloakUser getKeycloakUser(String username) {
        UserRepresentation user = keycloakUsersService.getUserByUsername(username);
        return keycloakUserMapper.toListKeycloakUser(user);
    }

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<ListBossesDto> entitiesToShowDtos(List<BossesModel> listEntities);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakUsername", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract BossesModel toEntity(CreateBossesDto dto);

    @Override
    @Mapping(target = "bossesBusinesses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "keycloakUsername", ignore = true)
    public abstract BossesModel updateFromDto(UpdateBossesDto dto, @MappingTarget BossesModel entity);

}
