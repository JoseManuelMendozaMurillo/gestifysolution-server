package com.ventuit.adminstrativeapp.warehouses.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;

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
public class UpdateWarehousesCategoriesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "The name must not exceed 60 characters.")
    @Unique(model = WarehousesCategoriesModel.class, fieldName = "name", message = "This category is already registered")
    private String name;

    @Size(max = 100, message = "The description must not exceed 100 characters.")
    private String description;

}
