package com.ventuit.adminstrativeapp.businesses.serialization;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesTypeRepository;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BusinessesTypeModelDeserializer extends JsonDeserializer<BusinessesTypeModel> {

    private final BusinessesTypeRepository businessesTypeRepository;

    @Override
    public BusinessesTypeModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer businessesTypeId = jsonParser.getIntValue();
        return businessesTypeRepository.findById(businessesTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Businesses type", businessesTypeId));
    }

}
