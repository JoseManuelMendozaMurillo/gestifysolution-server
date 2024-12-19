package com.ventuit.adminstrativeapp.warehouses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.warehouses.dto.CreateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.dto.UpdateWarehousesCategoriesDto;
import com.ventuit.adminstrativeapp.warehouses.services.WarehousesCategoriesService;

@RestController
@RequestMapping("warehouses-categories")
public class WarehousesCategoriesController extends
        CrudControllerImpl<CreateWarehousesCategoriesDto, UpdateWarehousesCategoriesDto, CreateWarehousesCategoriesDto, Integer, WarehousesCategoriesService> {

    public WarehousesCategoriesController(WarehousesCategoriesService crudService) {
        super(crudService);
    }
}
