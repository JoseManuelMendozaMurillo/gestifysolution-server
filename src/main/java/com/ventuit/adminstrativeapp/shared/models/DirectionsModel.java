package com.ventuit.adminstrativeapp.shared.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

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
@Table(name = "directions")
public class DirectionsModel extends ExtendedBaseModel {

    @Column(length = 60, nullable = false)
    private String latitude;

    @Column(length = 60, nullable = false)
    private String longitude;

    @Column(length = 60, nullable = false)
    private String street;

    @Column(length = 10, nullable = false)
    private String exteriorNumber;

    @Column(length = 10)
    private String exteriorLetter;

    @Column(length = 10)
    private String interiorNumber;

    @Column(length = 10)
    private String interiorLetter;

    @Column(length = 60)
    private String neighborhood;

    @Column(length = 20, nullable = false)
    private String postalCode;

    @Column(length = 60, nullable = false)
    private String city;

    @Column(length = 60, nullable = false)
    private String state;

    @Column(length = 60, nullable = false)
    private String country;

}