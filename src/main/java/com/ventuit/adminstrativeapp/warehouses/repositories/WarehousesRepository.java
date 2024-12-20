package com.ventuit.adminstrativeapp.warehouses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesModel;

@Repository
public interface WarehousesRepository extends BaseRepository<WarehousesModel, Integer> {

}
