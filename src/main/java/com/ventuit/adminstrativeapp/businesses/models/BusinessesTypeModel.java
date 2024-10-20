package com.ventuit.adminstrativeapp.businesses.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "businesses_type")
public class BusinessesTypeModel extends ExtendedBaseModel {

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(length = 60, nullable = false, unique = true)
    private String type;

    @Column(length = 200, nullable = false)
    private String description;

}
