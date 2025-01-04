package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliesService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.ListSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesDto;

@RestController
@RequestMapping("supplies")
public class SuppliesController
        extends CrudControllerImpl<CreateSuppliesDto, UpdateSuppliesDto, ListSuppliesDto, Integer, SuppliesService> {

    public SuppliesController(SuppliesService service) {
        super(service);
    }
}
