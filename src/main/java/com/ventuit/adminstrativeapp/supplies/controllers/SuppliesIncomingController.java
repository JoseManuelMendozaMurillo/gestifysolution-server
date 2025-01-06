package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliesIncomingService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesIncomingDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesIncomingDto;

@RestController
@RequestMapping("supplies-incoming")
public class SuppliesIncomingController extends
        CrudControllerImpl<CreateSuppliesIncomingDto, UpdateSuppliesIncomingDto, CreateSuppliesIncomingDto, Integer, SuppliesIncomingService> {

    public SuppliesIncomingController(SuppliesIncomingService service) {
        super(service);
    }

}
