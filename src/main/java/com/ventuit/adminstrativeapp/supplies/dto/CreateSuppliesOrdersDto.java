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

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Status cannot be null")
    private SuppliesOrdersStatus status;

    @Size(max = 60, message = "Description cannot exceed 60 characters")
    private String description;

    @NotNull(message = "Supplies cannot be null")
    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;

    @NotNull(message = "Supplier cannot be null")
    @JsonDeserialize(using = SuppliersDeserializer.class)
    private SuppliersModel supplier;
}
