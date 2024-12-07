package com.ventuit.adminstrativeapp.branches.services;

import com.ventuit.adminstrativeapp.branches.dto.BranchesDto;
import com.ventuit.adminstrativeapp.branches.mappers.BranchesMapper;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;

public class BranchesService
        extends CrudServiceImpl<BranchesDto, BranchesModel, Integer, BranchesMapper, BranchesRepository> {

    public BranchesService(BranchesRepository repository, BranchesMapper mapper) {
        super(repository, mapper);
    }
}
