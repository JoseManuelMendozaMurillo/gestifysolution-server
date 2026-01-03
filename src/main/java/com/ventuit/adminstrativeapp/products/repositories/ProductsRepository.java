package com.ventuit.adminstrativeapp.products.repositories;

import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends BaseRepository<ProductsModel, Integer> {
    boolean existsById(Integer id);

    boolean existsByIdAndDeletedAtIsNull(Integer id);

    @Query("SELECT DISTINCT p FROM ProductsModel p " +
            "JOIN p.branchesProducts bp " +
            "JOIN bp.branch b " +
            "JOIN b.business bu " +
            "WHERE bu.id = :businessId " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND p.deletedAt IS NULL " +
            "AND p.active = true")
    Page<ProductsModel> findDistinctProductsByBusinessId(
            @Param("businessId") Integer businessId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}
