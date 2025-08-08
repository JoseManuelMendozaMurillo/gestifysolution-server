package com.ventuit.adminstrativeapp.products.repositories;

import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsImagesRepository extends BaseRepository<ProductsImagesModel, Integer> {

}
