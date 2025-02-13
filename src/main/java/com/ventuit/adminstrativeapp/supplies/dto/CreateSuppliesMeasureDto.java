package com.ventuit.adminstrativeapp.supplies.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;

import jakarta.validation.constraints.NotBlank;
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
public class CreateSuppliesMeasureDto extends ExtendedBaseDto {

    @NotBlank(message = "You must send the supplies measure's name")
    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Unique(model = SuppliesMeasureModel.class, fieldName = "measure", message = "This measure is already registered")
    private String measure;

}
