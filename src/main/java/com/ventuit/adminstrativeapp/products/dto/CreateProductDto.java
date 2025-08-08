package com.ventuit.adminstrativeapp.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.serialization.ProductsCategoriesModelDeserializer;
import com.ventuit.adminstrativeapp.products.validations.onlyoneportraitimage.OnlyOnePortraitImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductDto {
    @NotBlank(message = "You must send the product name")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "You must send the price")
    private Double price;

    @NotNull(message = "You must specify if the product is active")
    private Boolean active;

    @NotNull(message = "You must send the product category")
    @JsonDeserialize(using = ProductsCategoriesModelDeserializer.class)
    private ProductsCategoriesModel category;

    @Valid
    @NotEmpty(message = "You must send at least one image")
    @OnlyOnePortraitImage
    private List<CreateProductImageDto> images;
}
