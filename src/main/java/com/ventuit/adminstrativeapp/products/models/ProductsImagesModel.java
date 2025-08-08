package com.ventuit.adminstrativeapp.products.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true, exclude = "product")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products_images")
public class ProductsImagesModel extends ExtendedBaseModel {
    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductsModel product;

    @OneToOne(optional = false)
    @JoinColumn(name = "file_id", nullable = false, unique = true)
    private FilesModel file;

    @Column(nullable = false)
    private boolean portrait;
}
