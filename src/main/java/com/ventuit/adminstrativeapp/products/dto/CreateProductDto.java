package com.ventuit.adminstrativeapp.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.products.validations.onlyoneportraitimage.OnlyOnePortraitImage;
import com.ventuit.adminstrativeapp.shared.validations.exist.Exist;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductDto {
    @NotBlank(message = "{CreateProductDto.name.NotBlank}")
    @Size(max = 100, message = "{CreateProductDto.name.Size}")
    @Unique(model = ProductsModel.class, fieldName = "name", message = "{CreateProductDto.name.Unique}")
    private String name;

    @Size(max = 500, message = "{CreateProductDto.description.Size}")
    private String description;

    @NotNull(message = "{CreateProductDto.price.NotNull}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{CreateProductDto.price.DecimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{CreateProductDto.price.Digits}")
    private Double price;

    @NotNull(message = "{CreateProductDto.active.NotNull}")
    private Boolean active;

    @NotNull(message = "{CreateProductDto.categoryId.NotNull}")
    @Min(value = 1, message = "{CreateProductDto.categoryId.Min}")
    @Exist(repository = ProductsCategoriesRepository.class, method = "existsById", message = "{CreateProductDto.categoryId.Exist}", paramType = Integer.class)
    private Integer categoryId;

    @Valid
    @NotEmpty(message = "{CreateProductDto.images.NotEmpty}")
    @OnlyOnePortraitImage(message = "{CreateProductDto.images.OnlyOnePortraitImage}")
    private List<CreateProductImageDto> images;
}
