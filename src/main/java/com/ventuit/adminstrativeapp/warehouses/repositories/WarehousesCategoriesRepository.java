package com.ventuit.adminstrativeapp.warehouses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;

@Repository
public interface WarehousesCategoriesRepository extends BaseRepository<WarehousesCategoriesModel, Integer> {

}
