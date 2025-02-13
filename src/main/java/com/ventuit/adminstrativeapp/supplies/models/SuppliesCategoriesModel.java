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
@Table(name = "supplies_categories")
public class SuppliesCategoriesModel extends ExtendedBaseModel {

    @Column(length = 60, unique = true, nullable = false)
    @Unique(model = SuppliesCategoriesModel.class, fieldName = "name", message = "This category is already registered")
    private String category;

    @Column(length = 60)
    private String description;

}
