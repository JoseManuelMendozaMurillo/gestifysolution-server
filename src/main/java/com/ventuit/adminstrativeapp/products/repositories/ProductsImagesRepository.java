package com.ventuit.adminstrativeapp.products.repositories;

import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsImagesRepository extends BaseRepository<ProductsImagesModel, Integer> {

    @Modifying
    @Query("UPDATE ProductsImagesModel pi SET pi.portrait = false WHERE pi.product.id = :productId")
    void setPortraitToFalseForProduct(@Param("productId") Integer productId);

}
