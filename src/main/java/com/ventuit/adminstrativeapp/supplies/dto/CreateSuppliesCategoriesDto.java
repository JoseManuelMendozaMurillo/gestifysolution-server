package com.ventuit.adminstrativeapp.supplies.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateSuppliesCategoriesDto extends ExtendedBaseDto {

    @NotBlank(message = "{SuppliesCategory.category.NotBlank}")
    @Size(max = 60, message = "{SuppliesCategory.category.Size}")
    @Unique(model = SuppliesCategoriesModel.class, fieldName = "name", message = "{SuppliesCategory.category.Unique}")
    private String category;

    @Size(max = 60, message = "{SuppliesCategory.description.Size}")
    private String description;

}
