package com.ventuit.adminstrativeapp.warehouses.services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.mappers.WarehousesCategoriesMapper;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;
import com.ventuit.adminstrativeapp.warehouses.repositories.WarehousesCategoriesRepository;

@Service
public class WarehousesCategoriesService extends
        CrudServiceImpl<CreateWarehousesCategoriesDto, UpdateWarehousesCategoriesDto, CreateWarehousesCategoriesDto, WarehousesCategoriesModel, Integer, WarehousesCategoriesMapper, WarehousesCategoriesRepository> {

    public WarehousesCategoriesService(WarehousesCategoriesRepository repository, WarehousesCategoriesMapper mapper) {
        super(repository, mapper);
    }
}
