package com.ventuit.adminstrativeapp.businesses.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.serialization.BusinessesTypeModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.IndustriesModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.TypesRegimensTaxesModelDeserializer;
import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.rfc.Rfc;

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
public class UpdateBusinessesDto extends BaseDto {

    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters long")
    @Rfc
    private String rfc;

    @PastOrPresentDate(message = "Establishment date must be in the past or present")
    private LocalDate establishmentDate;

    @JsonDeserialize(using = IndustriesModelDeserializer.class)
    private IndustriesModel industry;

    @JsonDeserialize(using = BusinessesTypeModelDeserializer.class)
    private BusinessesTypeModel businessesType;

    @JsonDeserialize(using = TypesRegimensTaxesModelDeserializer.class)
    private TypesRegimensTaxesModel taxRegimen;
}
