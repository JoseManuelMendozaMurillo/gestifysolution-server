package com.ventuit.adminstrativeapp.products.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;

@Mapper(componentModel = ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductsImagesMapper {

        @Autowired
        private FilesServiceImpl filesService;
        @Autowired
        private ProductsRepository productsRepository;

        @Named("toShowDto")
        @Mapping(target = "image", source = "file", qualifiedByName = "filesModelToFileResponseDto")
        public abstract ListProductsImagesDto toShowDto(ProductsImagesModel entity);

        @IterableMapping(qualifiedByName = "toShowDto")
        public List<ListProductsImagesDto> entitiesToShowDtos(List<ProductsImagesModel> listEntities) {
                return listEntities.stream()
                                .map(this::toShowDto)
                                .toList();
        }

        @Mappings({
                        @Mapping(target = "product", source = "productId", qualifiedByName = "productIdToProductModel"),
                        @Mapping(target = "file", ignore = true),
                        @Mapping(target = "id", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "updatedAt", ignore = true),
                        @Mapping(target = "deletedAt", ignore = true),
                        @Mapping(target = "createdBy", ignore = true),
                        @Mapping(target = "updatedBy", ignore = true),
                        @Mapping(target = "deletedBy", ignore = true)
        })
        public abstract ProductsImagesModel toEntity(CreateProductsImagesDto dto);

        @Named("filesModelToFileResponseDto")
        public FileResponseDto filesModelToFileResponseDto(FilesModel model) {
                return filesService.getFile(model.getId());
        }

        @Named("productIdToProductModel")
        public ProductsModel productIdToProductModel(Integer id) {
                return productsRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        }

}
