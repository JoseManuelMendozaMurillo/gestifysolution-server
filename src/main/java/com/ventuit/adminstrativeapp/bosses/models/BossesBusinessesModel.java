package com.ventuit.adminstrativeapp.bosses.models;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "bosses_businesses")
public class BossesBusinessesModel extends ExtendedBaseModel {

    @ManyToOne()
    @JoinColumn(name = "boss_id", nullable = false)
    private BossesModel boss;

    @ManyToOne()
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessesModel businesses;

}
