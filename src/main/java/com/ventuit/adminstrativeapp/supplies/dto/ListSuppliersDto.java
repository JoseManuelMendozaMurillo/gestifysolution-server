package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;

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
public class ListSuppliersDto extends ExtendedBaseDto {

    private String name;

    private String phone;

    private String email;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    private DirectionsDto direction;

    private SuppliesModel supplies;
}
