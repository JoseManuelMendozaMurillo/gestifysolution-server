package com.ventuit.adminstrativeapp.branches.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.branches.dto.BranchesDto;
import com.ventuit.adminstrativeapp.branches.services.BranchesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("branches")
public class BranchesController
        extends CrudControllerImpl<BranchesDto, BranchesDto, BranchesDto, Integer, BranchesService> {

    public BranchesController(BranchesService crudService) {
        super(crudService);
    }

}
