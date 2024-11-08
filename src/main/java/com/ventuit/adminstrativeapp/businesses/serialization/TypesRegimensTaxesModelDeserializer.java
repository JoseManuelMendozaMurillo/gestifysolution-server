package com.ventuit.adminstrativeapp.businesses.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.TypesRegimensTaxesRepository;
import com.ventuit.adminstrativeapp.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TypesRegimensTaxesModelDeserializer extends JsonDeserializer<TypesRegimensTaxesModel> {

    private final TypesRegimensTaxesRepository typesRegimensTaxesRepository;

    @Override
    public TypesRegimensTaxesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer taxRegimenId = jsonParser.getIntValue();
        return typesRegimensTaxesRepository.findById(taxRegimenId)
                .orElseThrow(() -> new EntityNotFoundException("Tax regimens", taxRegimenId));
    }

}
