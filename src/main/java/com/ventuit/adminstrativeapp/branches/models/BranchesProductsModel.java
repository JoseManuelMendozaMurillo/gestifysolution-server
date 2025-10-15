package com.ventuit.adminstrativeapp.branches.models;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;

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
@Table(name = "branches_products")
public class BranchesProductsModel extends ExtendedBaseModel {

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchesModel branch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductsModel product;
}
