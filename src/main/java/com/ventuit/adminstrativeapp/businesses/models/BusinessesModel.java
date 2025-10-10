package com.ventuit.adminstrativeapp.businesses.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Check;

import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.rfc.Rfc;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "businesses")
@Check(name = "businesses_rfc_ck_01", constraints = "CHAR_LENGTH(rfc) IN (12, 13)") // Check constraint for RFC length
public class BusinessesModel extends ExtendedBaseModel {
    // TODO: Check relationships of this table

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 200, nullable = true)
    private String description;

    @Column(length = 13, nullable = false, unique = true)
    @NotBlank(message = "RFC cannot be null")
    @Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters long")
    @Rfc
    @Unique(model = BusinessesModel.class, fieldName = "rfc", message = "This rfc is already registered")
    private String rfc;

    @Column()
    @PastOrPresentDate(message = "Establishment date must be in the past or present")
    private LocalDate establishmentDate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "industry_id", nullable = false, unique = false)
    private IndustriesModel industry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "business_type_id", nullable = false, unique = false)
    private BusinessesTypeModel businessesType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tax_regimen_id", nullable = false, unique = false)
    private TypesRegimensTaxesModel taxRegimen;

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<BossesBusinessesModel> bossesBusinesses;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<BranchesModel> branches;

    @OneToOne(optional = true)
    @JoinColumn(name = "logo_id", nullable = true, unique = false)
    private FilesModel logo;
}
