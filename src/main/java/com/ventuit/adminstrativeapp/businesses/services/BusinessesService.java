package com.ventuit.adminstrativeapp.businesses.services;

import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.mappers.BusinessesMapper;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;

@Service
public class BusinessesService
        extends
        CrudServiceImpl<CreateBusinessesDto, UpdateBusinessesDto, CreateBusinessesDto, BusinessesModel, Integer, BusinessesMapper, BusinessesRepository> {

    public BusinessesService(BusinessesRepository repository, BusinessesMapper mapper) {
        super(repository, mapper);
    }

}
