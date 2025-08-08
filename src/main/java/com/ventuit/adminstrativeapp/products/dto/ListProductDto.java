package com.ventuit.adminstrativeapp.products.dto;

import java.util.List;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;

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
public class ListProductDto extends ExtendedBaseDto {
    private String name;

    private String description;

    private Double price;

    private Boolean active;

    private ListProductsCategoryDto category;

    private List<ListProductImageDto> images;
}
