package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface BusinessesRepository extends BaseRepository<BusinessesModel, Integer> {
    boolean existsById(Integer id);

    boolean existsByIdAndDeletedAtIsNull(Integer id);

    @Query("SELECT bb.businesses FROM BossesBusinessesModel bb " +
            "WHERE bb.boss.id = :bossId " +
            "AND bb.deletedAt IS NULL " +
            "AND bb.businesses.deletedAt IS NULL")
    Page<BusinessesModel> findBusinessesByBossId(@Param("bossId") Integer bossId, Pageable pageable);
}
