package com.ventuit.adminstrativeapp.warehouses.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder()
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateWarehousesCategoriesDto extends ExtendedBaseDto {

    @NotBlank(message = "The name field cannot be empty or null.")
    @Size(max = 60, message = "The name must not exceed 60 characters.")
    private String name;

    @Size(max = 100, message = "The description must not exceed 100 characters.")
    private String description;
}
