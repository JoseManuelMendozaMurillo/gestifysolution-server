package com.ventuit.adminstrativeapp.warehouses.models;

import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "warehouses")
public class WarehousesModel extends ExtendedBaseModel {

    @Column(length = 60, nullable = false, unique = true)
    private String name;

    @Column(length = 30, nullable = true, unique = true)
    @Phone
    private String phone;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private WarehousesCategoriesModel category;

}
