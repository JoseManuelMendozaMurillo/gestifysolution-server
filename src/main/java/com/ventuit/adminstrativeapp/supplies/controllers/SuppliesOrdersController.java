package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliesOrdersService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesOrdersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesOrdersDto;

@RestController
@RequestMapping("supplies-orders")
public class SuppliesOrdersController extends
        CrudControllerImpl<CreateSuppliesOrdersDto, UpdateSuppliesOrdersDto, CreateSuppliesOrdersDto, Integer, SuppliesOrdersService> {

    public SuppliesOrdersController(SuppliesOrdersService service) {
        super(service);
    }
}
