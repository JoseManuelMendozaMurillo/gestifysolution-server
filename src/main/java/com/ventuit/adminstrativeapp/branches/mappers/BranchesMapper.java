package com.ventuit.adminstrativeapp.branches.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ventuit.adminstrativeapp.branches.dto.CreateBranchesDto;
import com.ventuit.adminstrativeapp.branches.dto.UpdateBranchesDto;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;

import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BranchesMapper
        implements CrudMapperInterface<CreateBranchesDto, UpdateBranchesDto, CreateBranchesDto, BranchesModel> {

    @Autowired
    private BusinessesRepository businessesRepository;

    @Override
    @Named("toDto")
    @Mapping(target = "businessId", source = "business.id")
    public abstract CreateBranchesDto toDto(BranchesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateBranchesDto> entitiesToDtos(List<BranchesModel> listEntities);

    @Override
    @Named("toShowDto")
    @Mapping(target = "businessId", source = "business.id")
    public abstract List<CreateBranchesDto> entitiesToShowDtos(List<BranchesModel> listEntities);

    @Override
    @Mapping(target = "businessId", source = "business.id")
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract CreateBranchesDto toShowDto(BranchesModel entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", source = "businessId", qualifiedByName = "businessIdToBusinessModel")
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
    @Mapping(target = "business", ignore = true)
    public abstract BranchesModel updateFromDto(UpdateBranchesDto dto, @MappingTarget BranchesModel entity);

    @Named("businessIdToBusinessModel")
    public BusinessesModel businessIdToBusinessModel(Integer businessId) {
        return businessId == null ? null
                : businessesRepository.findById(businessId)
                        .orElseThrow(() -> new EntityNotFoundException("Business not found"));
    }
}
