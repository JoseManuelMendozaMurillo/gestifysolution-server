package com.ventuit.adminstrativeapp.supplies.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "suppliers")
public class SuppliersModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true, length = 60)
    @Unique(model = SuppliersModel.class, fieldName = "name", message = "This supplier is already registered")
    private String name;

    @Column(nullable = true, unique = true, length = 30)
    @Phone
    @Unique(model = SuppliersModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @Column(nullable = true, unique = true, length = 60)
    @Email
    @Unique(model = SuppliersModel.class, fieldName = "email", message = "This email is already registered")
    private String email;

    @Column(nullable = false, name = "quantity_per_unit")
    private Integer quantityPerUnit;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "direction_id", nullable = true)
    private DirectionsModel direction;

    @ManyToOne
    @JoinColumn(name = "supplies_id", nullable = false)
    @JsonBackReference
    private SuppliesModel supplies;

}