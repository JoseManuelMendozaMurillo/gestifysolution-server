package com.ventuit.adminstrativeapp.branches.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ventuit.adminstrativeapp.branches.dto.BranchesDto;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class BranchesMapper implements CrudMapperInterface<BranchesDto, BranchesModel> {
    @Override
    public abstract List<BranchesDto> entitiesToDtos(List<BranchesModel> listEntities);

    @Override
    public abstract BranchesDto toDto(BranchesModel entity);

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
    public abstract BranchesModel updateFromDto(BranchesDto dto, @MappingTarget BranchesModel entity);
}
