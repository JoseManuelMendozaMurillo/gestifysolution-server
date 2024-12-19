package com.ventuit.adminstrativeapp.warehouses.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

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
@Table(name = "warehouses_categories")
public class WarehousesCategoriesModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(nullable = true, length = 100)
    private String description;
}
