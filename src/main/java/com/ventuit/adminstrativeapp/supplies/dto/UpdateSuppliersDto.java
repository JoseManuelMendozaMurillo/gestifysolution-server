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

    @Size(max = 60, message = "{Supplier.name.Size}")
    @Unique(model = SuppliersModel.class, fieldName = "name", message = "{Supplier.name.Unique}")
    private String name;

    @Phone
    @Size(max = 30, message = "{Supplier.phone.Size}")
    @Unique(model = SuppliersModel.class, fieldName = "phone", message = "{Supplier.phone.Unique}")
    private String phone;

    @Email
    @Size(max = 60, message = "{Supplier.email.Size}")
    @Unique(model = SuppliersModel.class, fieldName = "email", message = "{Supplier.email.Unique}")
    private String email;

    private Integer quantityPerUnit;

    private Float price;

    @Valid
    private DirectionsDto direction;

    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;
}
