package com.ventuit.adminstrativeapp.bosses.models;

import java.time.LocalDate;
import java.util.Set;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "bosses")
public class BossesModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true)
    private String keycloakUsername;

    @Column(length = 30, nullable = true, unique = true)
    @Phone
    @Unique(model = BossesModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @Column(nullable = true)
    @PastOrPresentDate
    private LocalDate birthdate;

    @OneToMany(mappedBy = "boss", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<BossesBusinessesModel> bossesBusinesses;

}
