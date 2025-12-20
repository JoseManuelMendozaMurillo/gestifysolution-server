package com.ventuit.adminstrativeapp.products.repositories;

import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsCategoriesRepository extends BaseRepository<ProductsCategoriesModel, Integer> {
    boolean existsById(Integer id);

    boolean existsByIdAndDeletedAtIsNull(Integer id);
}
