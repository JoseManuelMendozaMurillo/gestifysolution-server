package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;

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
public class ListSuppliesDto extends ExtendedBaseDto {

    private String name;

    private String description;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    private SuppliesMeasureModel measure;

    private SuppliesCategoriesModel category;

    private List<SuppliersModel> supplier;
}
