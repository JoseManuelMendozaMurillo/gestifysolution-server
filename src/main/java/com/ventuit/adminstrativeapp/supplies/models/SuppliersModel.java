package com.ventuit.adminstrativeapp.supplies.models;

import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

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
@Table(name = "suppliers")
public class SuppliersModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(nullable = true, unique = true, length = 30)
    @Phone
    private String phone;

    @Column(nullable = true, unique = true, length = 60)
    @Email
    private String email;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

}