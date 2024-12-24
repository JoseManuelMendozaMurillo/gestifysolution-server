package com.ventuit.adminstrativeapp.supplies.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;

@Repository
public interface SuppliesMeasureRepository extends BaseRepository<SuppliesMeasureModel, Integer> {

}
