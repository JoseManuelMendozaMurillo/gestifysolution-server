package com.ventuit.adminstrativeapp.supplies.Services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesIncomingDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesIncomingDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliesIncomingMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesIncomingModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesIncomingRepository;

@Service
public class SuppliesIncomingService extends
        CrudServiceImpl<CreateSuppliesIncomingDto, UpdateSuppliesIncomingDto, CreateSuppliesIncomingDto, SuppliesIncomingModel, Integer, SuppliesIncomingMapper, SuppliesIncomingRepository> {

    public SuppliesIncomingService(SuppliesIncomingRepository repository, SuppliesIncomingMapper mapper) {
        super(repository, mapper);
    }
}
