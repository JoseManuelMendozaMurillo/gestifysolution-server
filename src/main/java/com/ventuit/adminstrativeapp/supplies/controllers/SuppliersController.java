package com.ventuit.adminstrativeapp.supplies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.supplies.Services.SuppliersService;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliersDto;

@RestController
@RequestMapping("suppliers")
public class SuppliersController extends
        CrudControllerImpl<CreateSuppliersDto, UpdateSuppliersDto, CreateSuppliersDto, Integer, SuppliersService> {

    public SuppliersController(SuppliersService service) {
        super(service);
    }
}
