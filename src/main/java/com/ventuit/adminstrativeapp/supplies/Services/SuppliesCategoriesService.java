package com.ventuit.adminstrativeapp.supplies.Services;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesCategoriesDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliesCategoriesDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliesCategoriesMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesCategoriesRepository;

public class SuppliesCategoriesService extends
        CrudServiceImpl<CreateSuppliesCategoriesDto, UpdateSuppliesCategoriesDto, CreateSuppliesCategoriesDto, SuppliesCategoriesModel, Integer, SuppliesCategoriesMapper, SuppliesCategoriesRepository> {

    public SuppliesCategoriesService(SuppliesCategoriesRepository repository, SuppliesCategoriesMapper mapper) {
        super(repository, mapper);
    }
}
