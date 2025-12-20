package com.ventuit.adminstrativeapp.products.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;
import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductsCategoriesMapper implements
        CrudMapperInterface<CreateProductsCategoryDto, UpdateProductsCategoryDto, ListProductsCategoryDto, ProductsCategoriesModel> {

    @Autowired
    private FilesServiceImpl filesService;

    @Override
    @Named("toDto")
    @Mapping(target = "image", ignore = true)
    public abstract CreateProductsCategoryDto toDto(ProductsCategoriesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateProductsCategoryDto> entitiesToDtos(List<ProductsCategoriesModel> listEntities);

    @Override
    @Named("toShowDto")
    @Mapping(target = "image", source = "image", qualifiedByName = "filesModelToFileResponseDto")
    public abstract ListProductsCategoryDto toShowDto(ProductsCategoriesModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<ListProductsCategoryDto> entitiesToShowDtos(List<ProductsCategoriesModel> listEntities);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract ProductsCategoriesModel toEntity(CreateProductsCategoryDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract ProductsCategoriesModel updateFromDto(UpdateProductsCategoryDto dto,
            @MappingTarget ProductsCategoriesModel entity);

    @Named("filesModelToFileResponseDto")
    public FileResponseDto filesModelToFileResponseDto(FilesModel filesModel) {
        if (filesModel == null)
            return null;
        return filesService.getFile(filesModel.getId());
    }
}
