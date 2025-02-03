package com.ventuit.adminstrativeapp.businesses.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.serialization.BusinessesTypeModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.IndustriesModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.TypesRegimensTaxesModelDeserializer;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.rfc.Rfc;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

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
public class CreateBusinessesDto extends ExtendedBaseDto {

    @NotBlank(message = "You must send the business's name")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    @Unique(model = BusinessesModel.class, fieldName = "name", message = "This business is already registered")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotBlank(message = "You must send the business's rfc")
    @Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters long")
    @Rfc
    @Unique(model = BusinessesModel.class, fieldName = "rfc", message = "This rfc is already registered")
    private String rfc;

    @PastOrPresentDate(message = "Establishment date must be in the past or present")
    private LocalDate establishmentDate;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @NotNull(message = "You must send the business's industry")
    @JsonDeserialize(using = IndustriesModelDeserializer.class)
    private IndustriesModel industry;

    @NotNull(message = "You must send the business's type")
    @JsonDeserialize(using = BusinessesTypeModelDeserializer.class)
    private BusinessesTypeModel businessesType;

    @NotNull(message = "You must send the business's tax regimen")
    @JsonDeserialize(using = TypesRegimensTaxesModelDeserializer.class)
    private TypesRegimensTaxesModel taxRegimen;

    private Set<BossesBusinessesModel> bossesBusinesses;
}
