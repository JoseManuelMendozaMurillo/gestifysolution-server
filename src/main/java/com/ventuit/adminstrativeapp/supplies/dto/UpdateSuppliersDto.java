package com.ventuit.adminstrativeapp.supplies.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesDeserializer;

import jakarta.validation.Valid;
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
public class UpdateSuppliersDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Unique(model = SuppliersModel.class, fieldName = "name", message = "This supplier is already registered")
    private String name;

    @Phone
    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    @Unique(model = SuppliersModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @Email
    @Size(max = 60, message = "Email cannot exceed 60 characters")
    @Unique(model = SuppliersModel.class, fieldName = "email", message = "This email is already registered")
    private String email;

    private Integer quantityPerUnit;

    private Float price;

    @Valid
    private DirectionsDto direction;

    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;
}
