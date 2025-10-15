package com.ventuit.adminstrativeapp.products.mappers;

import java.util.List;
import java.util.Set;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.core.mappers.interfaces.CrudMapperInterface;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductsMapper
                implements CrudMapperInterface<CreateProductDto, UpdateProductDto, ListProductDto, ProductsModel> {

        @Autowired
        private ProductsCategoriesMapper productsCategoriesMapper;
        @Autowired
        private ProductsCategoriesRepository productsCategoriesRepository;
        @Autowired
        private FilesServiceImpl filesService;

        @Override
        @Named("toDto")
        @Mapping(target = "images", ignore = true)
        @Mapping(target = "categoryId", source = "category.id")
        public abstract CreateProductDto toDto(ProductsModel entity);

        @Override
        @IterableMapping(qualifiedByName = "toDto")
        public List<CreateProductDto> entitiesToDtos(List<ProductsModel> listEntities) {
                return listEntities.stream()
                                .map(this::toDto)
                                .toList();
        }

        @Override
        @Named("toShowDto")
        @Mapping(target = "category", source = "category", qualifiedByName = "categoryModelToListCategoryDto")
        @Mapping(target = "images", source = "images", qualifiedByName = "productsImagesModelToListProductImageDto")
        public abstract ListProductDto toShowDto(ProductsModel entity);

        @Override
        @IterableMapping(qualifiedByName = "toShowDto")
        public List<ListProductDto> entitiesToShowDtos(List<ProductsModel> listEntities) {
                return listEntities.stream()
                                .map(this::toShowDto)
                                .toList();
        }

        @Override
        @Mappings({
                        @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategoryModel"),
                        @Mapping(target = "images", ignore = true),
                        @Mapping(target = "branchesProducts", ignore = true),
                        @Mapping(target = "id", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "updatedAt", ignore = true),
                        @Mapping(target = "deletedAt", ignore = true),
                        @Mapping(target = "createdBy", ignore = true),
                        @Mapping(target = "updatedBy", ignore = true),
                        @Mapping(target = "deletedBy", ignore = true)
        })
        public abstract ProductsModel toEntity(CreateProductDto dto);

        @Override
        @Mappings({
                        @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategoryModel"),
                        @Mapping(target = "images", ignore = true),
                        @Mapping(target = "branchesProducts", ignore = true),
                        @Mapping(target = "id", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "updatedAt", ignore = true),
                        @Mapping(target = "deletedAt", ignore = true),
                        @Mapping(target = "createdBy", ignore = true),
                        @Mapping(target = "updatedBy", ignore = true),
                        @Mapping(target = "deletedBy", ignore = true)
        })
        public abstract ProductsModel updateFromDto(UpdateProductDto dto, @MappingTarget ProductsModel entity);

        @Named("categoryIdToCategoryModel")
        public ProductsCategoriesModel categoryIdToCategoryModel(Integer categoryId) {
                return categoryId == null ? null
                                : productsCategoriesRepository.findById(categoryId)
                                                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        @Named("categoryModelToListCategoryDto")
        public ListProductsCategoryDto categoryModelToListCategoryDto(ProductsCategoriesModel categoryModel) {
                return categoryModel == null ? null : productsCategoriesMapper.toShowDto(categoryModel);
        }

        @Named("productsImagesModelToListProductImageDto")
        public List<ListProductsImagesDto> productsImagesModelToListProductImageDto(
                        Set<ProductsImagesModel> productsImagesModels) {
                if (productsImagesModels == null || productsImagesModels.isEmpty()) {
                        return null;
                }

                return productsImagesModels.stream()
                                .map(this::mapProductImageToDto)
                                .toList();
        }

        private ListProductsImagesDto mapProductImageToDto(ProductsImagesModel model) {
                return ListProductsImagesDto.builder()
                                .id(model.getId())
                                .image(filesService.getFile(model.getFile().getId()))
                                .portrait(model.isPortrait())
                                .createdAt(model.getCreatedAt())
                                .updatedAt(model.getUpdatedAt())
                                .deletedAt(model.getDeletedAt())
                                .createdBy(model.getCreatedBy())
                                .updatedBy(model.getUpdatedBy())
                                .deletedBy(model.getDeletedBy())
                                .build();
        }
}
