package com.ventuit.adminstrativeapp.supplies.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesCategoriesDeserializer;
import com.ventuit.adminstrativeapp.supplies.serialization.SuppliesMeasureDeserializer;

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
public class CreateSuppliesDto extends ExtendedBaseDto {

    @NotBlank(message = "{Supply.name.NotBlank}")
    @Size(max = 60, message = "{Supply.name.Size}")
    @Unique(model = SuppliesModel.class, fieldName = "name", message = "{Supply.name.Unique}")
    private String name;

    @Size(max = 60, message = "{Supply.description.Size}")
    private String description;

    @NotNull(message = "{Supply.measure.NotNull}")
    @JsonDeserialize(using = SuppliesMeasureDeserializer.class)
    private SuppliesMeasureModel measure;

    @NotNull(message = "{Supply.category.NotNull}")
    @JsonDeserialize(using = SuppliesCategoriesDeserializer.class)
    private SuppliesCategoriesModel category;
}
