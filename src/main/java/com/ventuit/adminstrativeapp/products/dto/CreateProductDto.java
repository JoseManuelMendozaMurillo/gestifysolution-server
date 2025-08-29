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
    @NotBlank(message = "{Product.name.NotBlank}")
    @Size(max = 100, message = "{Product.name.Size}")
    @Unique(model = ProductsModel.class, fieldName = "name", message = "{Product.name.Unique}")
    private String name;

    @Size(max = 500, message = "{Product.description.Size}")
    private String description;

    @NotNull(message = "{Product.price.NotNull}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{Product.price.DecimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{Product.price.Digits}")
    private Double price;

    @NotNull(message = "{Product.active.NotNull}")
    private Boolean active;

    @NotNull(message = "{Product.categoryId.NotNull}")
    @Min(value = 1, message = "{Product.categoryId.Min}")
    @Exist(repository = ProductsCategoriesRepository.class, method = "existsByIdAndDeletedAtIsNull", message = "{Product.categoryId.Exist}", paramType = Integer.class)
    private Integer categoryId;

    @Valid
    @NotEmpty(message = "{Product.images.NotEmpty}")
    @OnlyOnePortraitImage(message = "{Product.images.OnlyOnePortraitImage}")
    private List<CreateProductImageDto> images;
}
