package com.ventuit.adminstrativeapp.supplies.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesCategoriesModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesCategoriesRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuppliesCategoriesDeserializer extends JsonDeserializer<SuppliesCategoriesModel> {

    private final SuppliesCategoriesRepository suppliesCategoriesRepository;

    @Override
    public SuppliesCategoriesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer suppliesCategoryId = jsonParser.getIntValue();

        return suppliesCategoriesRepository.findById(suppliesCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Supplies category", suppliesCategoryId));
    }

}
