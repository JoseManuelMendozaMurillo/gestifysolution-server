package com.ventuit.adminstrativeapp.supplies.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesCategoriesDeserializer;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesMeasureDeserializer;

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
public class UpdateSuppliesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Unique(model = SuppliesModel.class, fieldName = "name", message = "This supplies is already registered")
    private String name;

    @Size(max = 60, message = "Description cannot exceed 60 characters")
    private String description;

    @JsonDeserialize(using = SuppliesMeasureDeserializer.class)
    private SuppliesMeasureModel measure;

    @JsonDeserialize(using = SuppliesCategoriesDeserializer.class)
    private SuppliesCategoriesModel category;
}
