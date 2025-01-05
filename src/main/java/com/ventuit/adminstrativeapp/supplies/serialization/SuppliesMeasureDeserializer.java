package com.ventuit.adminstrativeapp.supplies.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesMeasureModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesMeasureRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SuppliesMeasureDeserializer extends JsonDeserializer<SuppliesMeasureModel> {

    private final SuppliesMeasureRepository suppliesMeasureRepository;

    @Override
    public SuppliesMeasureModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer suppliesMeasureId = jsonParser.getIntValue();
        return suppliesMeasureRepository.findById(suppliesMeasureId)
                .orElseThrow(() -> new EntityNotFoundException("Supplies measure", suppliesMeasureId));
    }

}
