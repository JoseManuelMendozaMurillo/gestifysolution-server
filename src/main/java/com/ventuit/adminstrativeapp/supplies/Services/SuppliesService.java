package com.ventuit.adminstrativeapp.supplies.Services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.ListSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliesMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesRepository;

@Service
public class SuppliesService extends
        CrudServiceImpl<CreateSuppliesDto, UpdateSuppliesDto, ListSuppliesDto, SuppliesModel, Integer, SuppliesMapper, SuppliesRepository> {

    public SuppliesService(SuppliesMapper mapper, SuppliesRepository repository) {
        super(repository, mapper);
    }

}
