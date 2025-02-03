package com.ventuit.adminstrativeapp.supplies.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "supplies_measure")
public class SuppliesMeasureModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true, length = 60)
    @Unique(model = SuppliesMeasureModel.class, fieldName = "measure", message = "This measure is already registered")
    private String measure;

}
