package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface BusinessesRepository extends BaseRepository<BusinessesModel, Integer> {
    boolean existsById(Integer id);

    boolean existsByIdAndDeletedAtIsNull(Integer id);
}
