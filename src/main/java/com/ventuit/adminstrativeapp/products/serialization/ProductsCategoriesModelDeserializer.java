package com.ventuit.adminstrativeapp.products.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProductsCategoriesModelDeserializer extends JsonDeserializer<ProductsCategoriesModel> {

    private final ProductsCategoriesRepository productsCategoriesRepository;

    @Override
    public ProductsCategoriesModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        Integer categoryId = jsonParser.getIntValue();
        return productsCategoriesRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Product category", categoryId));
    }
}
