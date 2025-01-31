package com.ventuit.adminstrativeapp.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
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
@MappedSuperclass
public class ExtendedBaseModel extends BaseModel {

    @Column(updatable = false, nullable = false)
    private String createdBy;

    @Column(insertable = false, nullable = true)
    private String updatedBy;

    @Column(insertable = false, nullable = true)
    private String deletedBy;
}
