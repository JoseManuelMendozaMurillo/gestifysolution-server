package com.ventuit.adminstrativeapp.businesses.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;

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
public class ListBusinessesDto extends ExtendedBaseDto {

    private String name;

    private String description;

    private FileResponseDto logo;

    private String rfc;

    private LocalDate establishmentDate;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    private IndustriesModel industry;

    private BusinessesTypeModel businessesType;

    private TypesRegimensTaxesModel taxRegimen;
}
