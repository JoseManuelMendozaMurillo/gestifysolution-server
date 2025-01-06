package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;

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
}
