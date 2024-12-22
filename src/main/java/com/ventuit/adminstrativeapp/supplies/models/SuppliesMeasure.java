package com.ventuit.adminstrativeapp.supplies.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

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
public class SuppliesMeasure extends ExtendedBaseModel {

    private String measure;

}
