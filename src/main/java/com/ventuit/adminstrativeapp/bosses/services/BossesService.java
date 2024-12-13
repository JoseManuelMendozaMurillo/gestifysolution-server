package com.ventuit.adminstrativeapp.bosses.services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.bosses.dto.BossesDto;
import com.ventuit.adminstrativeapp.bosses.mappers.BossesMapper;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.bosses.repositories.BossesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;

@Service
public class BossesService
        extends CrudServiceImpl<BossesDto, BossesDto, BossesDto, BossesModel, Integer, BossesMapper, BossesRepository> {

    public BossesService(BossesRepository repository, BossesMapper mapper) {
        super(repository, mapper);
    }

}
