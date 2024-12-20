package com.ventuit.adminstrativeapp.warehouses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesDto;
import com.ventuit.adminstrativeapp.warehouses.services.WarehousesService;

@RestController
@RequestMapping("warehouses")
public class WarehousesController extends
        CrudControllerImpl<CreateWarehousesDto, UpdateWarehousesDto, CreateWarehousesDto, Integer, WarehousesService> {

    public WarehousesController(WarehousesService crudService) {
        super(crudService);
    }
}
