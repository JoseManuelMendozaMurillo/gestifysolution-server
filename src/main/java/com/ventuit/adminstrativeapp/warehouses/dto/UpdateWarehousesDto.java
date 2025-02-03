package com.ventuit.adminstrativeapp.warehouses.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesModel;
import com.ventuit.adminstrativeapp.warehouses.serialization.WarehousesCategoriesDeserializer;

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
public class UpdateWarehousesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Unique(model = WarehousesModel.class, fieldName = "name", message = "This warehouse is already registered")
    private String name;

    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    @Phone
    @Unique(model = WarehousesModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @JsonDeserialize(using = WarehousesCategoriesDeserializer.class)
    private WarehousesCategoriesModel category;
}
