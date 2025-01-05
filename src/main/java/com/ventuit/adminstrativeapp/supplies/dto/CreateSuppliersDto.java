package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesDeserializer;

import jakarta.validation.Valid;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSuppliersDto extends ExtendedBaseDto {

    @NotBlank(message = "You must send the supplier's name")
    @Size(max = 60, message = "Name cannot exceed 60 characters")
    private String name;

    @Phone
    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    private String phone;

    @Email
    @Size(max = 60, message = "Email cannot exceed 60 characters")
    private String email;

    @NotNull(message = "Quantity per unit cannot be null")
    private Integer quantityPerUnit;

    @NotNull(message = "Price cannot be null")
    private Float price;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @Valid
    private DirectionsDto direction;

    @NotNull(message = "Supplies cannot be null")
    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;
}
