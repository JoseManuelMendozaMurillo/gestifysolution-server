package com.ventuit.adminstrativeapp.warehouses.services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.mappers.WarehousesMapper;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesModel;
import com.ventuit.adminstrativeapp.warehouses.repositories.WarehousesRepository;

@Service
public class WarehousesService extends
        CrudServiceImpl<CreateWarehousesDto, UpdateWarehousesDto, CreateWarehousesDto, WarehousesModel, Integer, WarehousesMapper, WarehousesRepository> {

    public WarehousesService(WarehousesRepository repository, WarehousesMapper mapper) {
        super(repository, mapper);
    }
}
