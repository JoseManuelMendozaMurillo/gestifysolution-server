package com.ventuit.adminstrativeapp.supplies.models;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "supplies_incoming")
public class SuppliesIncomingModel extends ExtendedBaseModel {

    @Column(nullable = true)
    @PastOrPresentDate()
    private LocalDate expirationDate;

    @Column(length = 60, nullable = true)
    private String sku;

    @Column(nullable = false, name = "quantity_per_unit")
    private Integer quantityPerUnit;

    @Column(nullable = false)
    private Float price;

    @OneToOne()
    private SuppliesModel supplies;

    @OneToOne()
    private SuppliersModel supplier;
}
