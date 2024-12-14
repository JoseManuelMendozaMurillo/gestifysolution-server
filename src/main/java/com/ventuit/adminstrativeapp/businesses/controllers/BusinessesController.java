package com.ventuit.adminstrativeapp.businesses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.services.BusinessesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("businesses")
public class BusinessesController
        extends
        CrudControllerImpl<CreateBusinessesDto, UpdateBusinessesDto, CreateBusinessesDto, Integer, BusinessesService> {

    public BusinessesController(BusinessesService crudService) {
        super(crudService);
    }

}
