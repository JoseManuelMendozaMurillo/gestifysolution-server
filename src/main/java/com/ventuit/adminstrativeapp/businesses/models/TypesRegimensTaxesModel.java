package com.ventuit.adminstrativeapp.businesses.models;

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
@Table(name = "tax_regimens")
public class TypesRegimensTaxesModel extends ExtendedBaseModel {

    @Column(length = 100, nullable = false, unique = true)
    private String regimen;

    @Column(length = 200, nullable = true)
    private String description;

}