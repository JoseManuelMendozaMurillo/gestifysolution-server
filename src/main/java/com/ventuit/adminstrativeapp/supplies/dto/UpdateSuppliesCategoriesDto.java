package com.ventuit.adminstrativeapp.supplies.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSuppliesCategoriesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Category cannot exceed 60 characters")
    private String category;

    @Size(max = 60, message = "Description cannot exceed 60 characters")
    private String description;

}
