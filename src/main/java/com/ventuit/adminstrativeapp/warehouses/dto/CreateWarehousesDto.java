package com.ventuit.adminstrativeapp.warehouses.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesModel;
import com.ventuit.adminstrativeapp.warehouses.serialization.WarehousesCategoriesDeserializer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateWarehousesDto extends ExtendedBaseDto {

    @NotBlank(message = "{Warehouse.name.NotBlank}")
    @Size(max = 60, message = "{Warehouse.name.Size}")
    @Unique(model = WarehousesModel.class, fieldName = "name", message = "{Warehouse.name.Unique}")
    private String name;

    @Size(max = 30, message = "{Warehouse.phone.Size}")
    @Phone
    @Unique(model = WarehousesModel.class, fieldName = "phone", message = "{Warehouse.phone.Unique}")
    private String phone;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @NotNull(message = "{Warehouse.category.NotNull}")
    @JsonDeserialize(using = WarehousesCategoriesDeserializer.class)
    private WarehousesCategoriesModel category;
}
