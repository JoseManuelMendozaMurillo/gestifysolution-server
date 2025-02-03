package com.ventuit.adminstrativeapp.supplies.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "supplies")
public class SuppliesModel extends ExtendedBaseModel {

    @Column(nullable = false, unique = true, length = 60)
    @Unique(model = SuppliesModel.class, fieldName = "name", message = "This supplies is already registered")
    private String name;

    @Column(nullable = true, length = 60)
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active;

    @Column(nullable = true, insertable = false)
    private Integer activeChangedBy;

    @Column(nullable = true, insertable = false)
    private LocalDateTime activeChangedAt;

    @OneToOne()
    @JoinColumn(name = "measure_id")
    private SuppliesMeasureModel measure;

    @OneToOne()
    @JoinColumn(name = "category_id")
    private SuppliesCategoriesModel category;

    @OneToMany(mappedBy = "supplies", cascade = CascadeType.REFRESH, orphanRemoval = false)
    @JsonManagedReference
    private List<SuppliersModel> suppliers;

}
