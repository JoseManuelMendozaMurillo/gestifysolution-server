package com.ventuit.adminstrativeapp.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedBaseDto extends BaseDto {

    @NotNull(message = "createdBy value is required")
    private Integer createdBy;

    private Integer updatedBy;

    private Integer deletedBy;
}
