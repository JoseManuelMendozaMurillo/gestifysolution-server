package com.ventuit.adminstrativeapp.supplies.Services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesOrdersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesOrdersDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliesOrdersMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesOrdersModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesOrdersRepository;

@Service
public class SuppliesOrdersService extends
        CrudServiceImpl<CreateSuppliesOrdersDto, UpdateSuppliesOrdersDto, CreateSuppliesOrdersDto, SuppliesOrdersModel, Integer, SuppliesOrdersMapper, SuppliesOrdersRepository> {

    public SuppliesOrdersService(SuppliesOrdersMapper mapper, SuppliesOrdersRepository repository) {
        super(repository, mapper);
    }
}
