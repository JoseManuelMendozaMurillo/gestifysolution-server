package com.ventuit.adminstrativeapp.branches.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "branches")
public class BranchesModel extends ExtendedBaseModel {

    @Column(length = 60, nullable = false)
    private String name;

    @Column(length = 30, nullable = true)
    @Phone
    private String phone;

    @Column(length = 60, nullable = true)
    @Email
    private String email;

    @Column(nullable = true)
    @PastOrPresentDate(message = "Opening date must be in the past or present")
    private LocalDate openingDate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

    @OneToOne()
    @JoinColumn(name = "direction_id", nullable = false)
    private DirectionsModel direction;
}
