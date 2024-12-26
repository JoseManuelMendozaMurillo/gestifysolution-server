package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliesCategoriesService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesCategoriesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesCategoriesDto;

@RestController
@RequestMapping("supplies-categories")
public class SuppliesCategoriesController extends
        CrudControllerImpl<CreateSuppliesCategoriesDto, UpdateSuppliesCategoriesDto, CreateSuppliesCategoriesDto, Integer, SuppliesCategoriesService> {

    public SuppliesCategoriesController(SuppliesCategoriesService service) {
        super(service);
    }
}
