package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliersDeserializer;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesDeserializer;

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
public class UpdateSuppliesIncomingDto extends ExtendedBaseDto {
    @PastOrPresentDate()
    private LocalDate expirationDate;

    @Size(max = 60, message = "Sku cannot exceed 60 characters")
    private String sku;

    private Integer quantityPerUnit;

    private Float price;

    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;

    @JsonDeserialize(using = SuppliersDeserializer.class)
    private SuppliersModel supplier;
}
