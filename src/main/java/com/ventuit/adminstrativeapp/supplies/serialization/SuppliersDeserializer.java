package com.ventuit.adminstrativeapp.supplies.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliersRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuppliersDeserializer extends JsonDeserializer<SuppliersModel> {

    private final SuppliersRepository suppliersRepository;

    @Override
    public SuppliersModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer suppliersId = jsonParser.getIntValue();
        return suppliersRepository.findById(suppliersId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier", suppliersId));
    }

}
