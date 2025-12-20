package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDateTime;

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

    @NotBlank(message = "{Supplier.name.NotBlank}")
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

    @NotNull(message = "{Supplier.quantityPerUnit.NotNull}")
    private Integer quantityPerUnit;

    @NotNull(message = "{Supplier.price.NotNull}")
    private Float price;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @Valid
    private DirectionsDto direction;

    @NotNull(message = "{Supplier.supplies.NotNull}")
    @JsonDeserialize(using = SuppliesDeserializer.class)
    private SuppliesModel supplies;
}
