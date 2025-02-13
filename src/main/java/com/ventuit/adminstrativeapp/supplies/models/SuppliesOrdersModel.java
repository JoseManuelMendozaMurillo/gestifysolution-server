package com.ventuit.adminstrativeapp.supplies.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.supplies.enums.SuppliesOrdersStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "supplies_orders")
public class SuppliesOrdersModel extends ExtendedBaseModel {
    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.ORDINAL)
    private SuppliesOrdersStatus status;

    @Column(length = 60, nullable = true)
    private String description;

    @OneToOne()
    private SuppliesModel supplies;

    @OneToOne()
    private SuppliersModel supplier;
}
