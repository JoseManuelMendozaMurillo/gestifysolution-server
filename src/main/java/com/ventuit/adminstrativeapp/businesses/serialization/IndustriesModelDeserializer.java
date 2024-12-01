package com.ventuit.adminstrativeapp.businesses.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.IndustriesRepository;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IndustriesModelDeserializer extends JsonDeserializer<IndustriesModel> {

    private final IndustriesRepository industryRepository;

    @Override
    public IndustriesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        Integer industryId = jsonParser.getIntValue();
        return industryRepository.findById(industryId)
                .orElseThrow(() -> new EntityNotFoundException("Industry", industryId));
    }

}
