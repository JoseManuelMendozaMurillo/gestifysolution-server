package com.ventuit.adminstrativeapp.products.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductsImagesDto {

    @NotNull(message = "{Product.image.portrait.NotNull}")
    private Boolean portrait;

}
