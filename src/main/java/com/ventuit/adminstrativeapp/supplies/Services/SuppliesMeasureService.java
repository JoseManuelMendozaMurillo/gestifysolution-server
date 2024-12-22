package com.ventuit.adminstrativeapp.supplies.Services;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliesMeasureDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliesMeasureMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesMeasureRepository;

public class SuppliesMeasureService extends
        CrudServiceImpl<CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, CreateSuppliesMeasureDto, SuppliesMeasureModel, Integer, SuppliesMeasureMapper, SuppliesMeasureRepository> {

    public SuppliesMeasureService(SuppliesMeasureRepository repository, SuppliesMeasureMapper mapper) {
        super(repository, mapper);
    }
}
