package com.ventuit.adminstrativeapp.supplies.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.supplies.enums.SuppliesOrdersStatus;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliersDeserializer;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesDeserializer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSuppliesOrdersDto extends ExtendedBaseDto {

    @NotNull(message = "{SuppliesOrder.quantity.NotNull}")
    private Integer quantity;

    @NotNull(message = "{SuppliesOrder.status.NotNull}")
    private SuppliesOrdersStatus status;

    @Size(max = 60, message = "{SuppliesOrder.description.Size}")
    private String description;

    @NotNull(message = "{SuppliesOrder.supplies.NotNull}")
    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;

    @NotNull(message = "{SuppliesOrder.supplier.NotNull}")
    @JsonDeserialize(using = SuppliersDeserializer.class)
    private SuppliersModel supplier;
}
