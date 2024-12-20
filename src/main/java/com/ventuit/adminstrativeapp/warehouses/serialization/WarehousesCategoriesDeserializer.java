package com.ventuit.adminstrativeapp.warehouses.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.warehouses.models.WarehousesCategoriesModel;
import com.ventuit.adminstrativeapp.warehouses.repositories.WarehousesCategoriesRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehousesCategoriesDeserializer extends JsonDeserializer<WarehousesCategoriesModel> {

    private final WarehousesCategoriesRepository warehousesCategoriesRepository;

    @Override
    public WarehousesCategoriesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer categoryId = jsonParser.getIntValue();
        return warehousesCategoriesRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse category", categoryId));
    }

}
