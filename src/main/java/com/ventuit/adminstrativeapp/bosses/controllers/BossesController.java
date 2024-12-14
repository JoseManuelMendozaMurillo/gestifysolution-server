package com.ventuit.adminstrativeapp.bosses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.services.BossesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("bosses")
public class BossesController
        extends CrudControllerImpl<CreateBossesDto, UpdateBossesDto, CreateBossesDto, Integer, BossesService> {

    public BossesController(BossesService crudService) {
        super(crudService);
    }

}
