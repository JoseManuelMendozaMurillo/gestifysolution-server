package com.ventuit.adminstrativeapp.products.repositories;

import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends BaseRepository<ProductsModel, Integer> {
    boolean existsById(Integer id);

    boolean existsByIdAndDeletedAtIsNull(Integer id);
}
