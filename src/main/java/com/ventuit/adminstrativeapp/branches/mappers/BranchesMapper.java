package com.ventuit.adminstrativeapp.branches.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ventuit.adminstrativeapp.branches.dto.BranchesDto;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BranchesMapper
        implements CrudMapperInterface<BranchesDto, BranchesDto, BranchesDto, BranchesModel> {

    @Override
    @Named("toDto")
    public abstract BranchesDto toDto(BranchesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<BranchesDto> entitiesToDtos(List<BranchesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract List<BranchesDto> entitiesToShowDtos(List<BranchesModel> listEntities);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract BranchesDto toShowDto(BranchesModel entity);

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract BranchesModel toEntity(BranchesDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "direction", ignore = true)
    public abstract BranchesModel updateFromDto(BranchesDto dto, @MappingTarget BranchesModel entity);
}
