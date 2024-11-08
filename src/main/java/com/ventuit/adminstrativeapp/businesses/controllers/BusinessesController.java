package com.ventuit.adminstrativeapp.businesses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.businesses.dto.BusinessesDto;
import com.ventuit.adminstrativeapp.businesses.services.BusinessesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("businesses")
public class BusinessesController extends CrudControllerImpl<BusinessesDto, Integer, BusinessesService> {

    public BusinessesController(BusinessesService crudService) {
        super(crudService);
    }

}
