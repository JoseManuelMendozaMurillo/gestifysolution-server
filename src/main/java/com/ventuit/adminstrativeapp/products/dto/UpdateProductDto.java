package com.ventuit.adminstrativeapp.products.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.serialization.ProductsCategoriesModelDeserializer;
import com.ventuit.adminstrativeapp.products.validations.onlyoneportraitimage.OnlyOnePortraitImage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProductDto {

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Double price;

    private Boolean active;

    @JsonDeserialize(using = ProductsCategoriesModelDeserializer.class)
    private ProductsCategoriesModel category;

    @Valid
    @OnlyOnePortraitImage
    private List<CreateProductImageDto> images;
}
