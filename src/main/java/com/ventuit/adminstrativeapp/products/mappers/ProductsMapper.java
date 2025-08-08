package com.ventuit.adminstrativeapp.products.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductImageDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductsMapper
        implements CrudMapperInterface<CreateProductDto, UpdateProductDto, ListProductDto, ProductsModel> {

    @Autowired
    private ProductsCategoriesMapper productsCategoriesMapper;
    @Autowired
    private FilesServiceImpl filesService;

    @Override
    @Named("toDto")
    @Mapping(target = "images", ignore = true)
    public abstract CreateProductDto toDto(ProductsModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<CreateProductDto> entitiesToDtos(List<ProductsModel> listEntities);

    @Override
    @Named("toShowDto")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryModelToListCategoryDto")
    @Mapping(target = "images", source = "images", qualifiedByName = "productsImagesModelToListProductImageDto")
    public abstract ListProductDto toShowDto(ProductsModel entity);

    @Override
    @IterableMapping(qualifiedByName = "toShowDto")
    public abstract List<ListProductDto> entitiesToShowDtos(List<ProductsModel> listEntities);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "images", ignore = true)
    public abstract ProductsModel toEntity(CreateProductDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    public abstract ProductsModel updateFromDto(UpdateProductDto dto, @MappingTarget ProductsModel entity);

    @Named("categoryModelToListCategoryDto")
    public ListProductsCategoryDto categoryModelToListCategoryDto(ProductsCategoriesModel categoryModel) {
        if (categoryModel == null)
            return null;
        return productsCategoriesMapper.toShowDto(categoryModel);
    }

    @Named("productsImagesModelToListProductImageDto")
    public List<ListProductImageDto> productsImagesModelToListProductImageDto(
            Set<ProductsImagesModel> productsImagesModels) {
        if (productsImagesModels == null || productsImagesModels.isEmpty()) {
            System.out.println("Products images models are empty or null: " + productsImagesModels);
            return null;
        }
        List<ListProductImageDto> productImagesDtos = new ArrayList<>();
        for (ProductsImagesModel productsImageModel : productsImagesModels) {
            FileResponseDto fileResponseDto = filesService.getFile(productsImageModel.getFile().getId());
            ListProductImageDto productImageDto = ListProductImageDto.builder()
                    .id(productsImageModel.getId())
                    .image(fileResponseDto)
                    .portrait(productsImageModel.isPortrait())
                    .createdAt(productsImageModel.getCreatedAt())
                    .updatedAt(productsImageModel.getUpdatedAt())
                    .deletedAt(productsImageModel.getDeletedAt())
                    .createdBy(productsImageModel.getCreatedBy())
                    .updatedBy(productsImageModel.getUpdatedBy())
                    .deletedBy(productsImageModel.getDeletedBy())
                    .build();
            productImagesDtos.add(productImageDto);
        }
        return productImagesDtos;
    }
}
