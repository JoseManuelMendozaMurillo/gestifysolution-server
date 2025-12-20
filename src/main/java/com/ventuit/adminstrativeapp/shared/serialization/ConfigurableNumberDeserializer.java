package com.ventuit.adminstrativeapp.shared.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ventuit.adminstrativeapp.shared.annotations.JsonNumber;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
public class ConfigurableNumberDeserializer extends JsonDeserializer<Number> implements ContextualDeserializer {

    private String errorMessage;
    private JsonNumber.NumberType numberType;

    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_NUMBER_FLOAT) && !p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            // Fails if the token isn't a number at all (e.g., "abc", true)
            throw MismatchedInputException.from(p, Number.class, errorMessage);
        }

        // Check if a decimal is provided for an integer-only field
        if (numberType == JsonNumber.NumberType.INTEGER) {
            JsonParser.NumberType detectedType = p.getNumberType();
            if (detectedType == JsonParser.NumberType.FLOAT || detectedType == JsonParser.NumberType.DOUBLE
                    || detectedType == JsonParser.NumberType.BIG_DECIMAL) {
                throw MismatchedInputException.from(p, Number.class, "The value must be a valid whole number.");
            }
        }

        // Let Jackson handle the final conversion to the specific type (Integer,
        // Double, etc.)
        // This is robust because it will correctly deserialize "123" to an Integer or a
        // Double,
        // but it will fail if you try to deserialize "123.45" into an Integer field.
        Class<?> targetType = ctxt.getContextualType().getRawClass();
        try {
            if (targetType == Integer.class)
                return p.getIntValue();
            if (targetType == Double.class)
                return p.getDoubleValue();
            if (targetType == Long.class)
                return p.getLongValue();
            if (targetType == BigDecimal.class)
                return p.getDecimalValue();
            if (targetType == Float.class)
                return p.getFloatValue();
            if (targetType == BigInteger.class)
                return p.getBigIntegerValue();
            if (targetType == Short.class)
                return p.getShortValue();
        } catch (IOException e) {
            throw MismatchedInputException.from(p, targetType, errorMessage);
        }

        return p.getNumberValue();
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property == null) {
            return this;
        }

        JsonNumber ann = property.getAnnotation(JsonNumber.class);
        if (ann != null) {
            return new ConfigurableNumberDeserializer(ann.message(), ann.type());
        }

        return this;
    }
}