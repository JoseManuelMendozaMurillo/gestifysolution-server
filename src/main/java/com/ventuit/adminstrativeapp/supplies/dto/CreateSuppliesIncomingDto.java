package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
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
public class CreateSuppliesIncomingDto extends ExtendedBaseDto {
    @PastOrPresentDate()
    private LocalDate expirationDate;

    @Size(max = 60, message = "Sku cannot exceed 60 characters")
    private String sku;

    @NotNull(message = "Quantity per unit cannot be null")
    private Integer quantityPerUnit;

    @NotNull(message = "Price cannot be null")
    private Float price;

    @NotNull(message = "Supplies cannot be null")
    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;

    @NotNull(message = "Supplier cannot be null")
    @JsonDeserialize(using = SuppliersDeserializer.class)
    private SuppliersModel supplier;
}
