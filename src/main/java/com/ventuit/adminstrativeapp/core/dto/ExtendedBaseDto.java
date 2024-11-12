package com.ventuit.adminstrativeapp.core.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "createdBy value is required")
    private Integer createdBy;

    private Integer updatedBy;

    private Integer deletedBy;
}
