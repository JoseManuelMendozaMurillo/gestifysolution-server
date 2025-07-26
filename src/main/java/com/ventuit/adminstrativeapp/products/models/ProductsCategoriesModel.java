package com.ventuit.adminstrativeapp.products.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;

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
@Table(name = "products_categories")
public class ProductsCategoriesModel extends ExtendedBaseModel {
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 200, nullable = true)
    private String description;

    @OneToOne(optional = false)
    @JoinColumn(name = "image_id", nullable = true, unique = false)
    private FilesModel image;
}
