package com.ventuit.adminstrativeapp.businesses.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.serialization.BusinessesTypeModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.IndustriesModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.TypesRegimensTaxesModelDeserializer;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class BusinessesDto extends ExtendedBaseDto {

    @NotBlank(message = "You must send the business's name")
    private String name;

    private String description;

    @NotBlank(message = "You must send the business's rfc")
    @Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters long")
    @Pattern(regexp = RegexUtils.RFC_PATTERN, message = "Invalid RFC format")
    private String rfc;

    private LocalDate establishmentDate;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @NotBlank(message = "You must send the business's industry")
    @JsonDeserialize(using = IndustriesModelDeserializer.class)
    private IndustriesModel industry;

    @NotBlank(message = "You must send the business's type")
    @JsonDeserialize(using = BusinessesTypeModelDeserializer.class)
    private BusinessesTypeModel businessesType;

    @NotBlank(message = "You must send the business's tax regimen")
    @JsonDeserialize(using = TypesRegimensTaxesModelDeserializer.class)
    private TypesRegimensTaxesModel taxRegimen;

    private Set<BossesBusinessesModel> bossesBusinesses;
}
