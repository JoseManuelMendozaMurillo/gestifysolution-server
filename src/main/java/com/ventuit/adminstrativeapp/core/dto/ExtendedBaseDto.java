package com.ventuit.adminstrativeapp.core.dto;

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

    private String createdBy;

    private String updatedBy;

    private String deletedBy;
}
