package com.ventuit.adminstrativeapp.businesses.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.services.BusinessesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("businesses")
public class BusinessesController
        extends
        CrudControllerImpl<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, Integer, BusinessesService> {

    public BusinessesController(BusinessesService crudService) {
        super(crudService);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListBusinessesDto> create(@ModelAttribute CreateBusinessesDto createBusinessesDto) {
        ListBusinessesDto entityCreated = this.crudService.create(createBusinessesDto);
        return ResponseEntity.ok(entityCreated);
    }

}
