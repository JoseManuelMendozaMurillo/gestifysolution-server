package com.ventuit.adminstrativeapp.shared.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.shared.serialization.ConfigurableNumberDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = ConfigurableNumberDeserializer.class)
public @interface JsonNumber {

    NumberType type() default NumberType.ANY;

    String message() default "The value must be a valid number.";

    enum NumberType {
        ANY,
        INTEGER, // For Integer, Long, Short, BigInteger
        DECIMAL // For Double, Float, BigDecimal
    }
}