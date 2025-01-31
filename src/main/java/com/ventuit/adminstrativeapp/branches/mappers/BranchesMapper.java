package com.ventuit.adminstrativeapp.branches.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ventuit.adminstrativeapp.branches.dto.CreateBranchesDto;
import com.ventuit.adminstrativeapp.branches.dto.UpdateBranchesDto;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BranchesMapper
        implements CrudMapperInterface<CreateBranchesDto, UpdateBranchesDto, CreateBranchesDto, BranchesModel> {

    @Override
    @Named("toDto")
    public abstract CreateBranchesDto toDto(BranchesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateBranchesDto> entitiesToDtos(List<BranchesModel> listEntities);

    @Override
    @Named("toShowDto")
    public abstract List<CreateBranchesDto> entitiesToShowDtos(List<BranchesModel> listEntities);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract CreateBranchesDto toShowDto(BranchesModel entity);

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract BranchesModel toEntity(CreateBranchesDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "direction", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activeChangedAt", ignore = true)
    @Mapping(target = "activeChangedBy", ignore = true)
    public abstract BranchesModel updateFromDto(UpdateBranchesDto dto, @MappingTarget BranchesModel entity);
}
