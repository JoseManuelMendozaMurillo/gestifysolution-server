package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliesMeasureService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesMeasureDto;

@RestController
@RequestMapping("supplies-measure")
public class SuppliesMeasureController
        extends
        CrudControllerImpl<CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, Integer, SuppliesMeasureService> {

    public SuppliesMeasureController(SuppliesMeasureService crudService) {
        super(crudService);
    }
}
