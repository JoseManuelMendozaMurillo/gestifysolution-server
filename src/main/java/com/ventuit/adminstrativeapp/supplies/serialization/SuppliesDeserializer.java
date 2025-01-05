package com.ventuit.adminstrativeapp.supplies.serialization;

import java.io.IOException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.supplies.models.SuppliesModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliesRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuppliesDeserializer extends JsonDeserializer<SuppliesModel> {

    private final SuppliesRepository suppliesRepository;

    @Override
    public SuppliesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer suppliesId = jsonParser.getIntValue();
        return suppliesRepository.findById(suppliesId)
                .orElseThrow(() -> new EntityNotFoundException("Supplies", suppliesId));
    }

}
