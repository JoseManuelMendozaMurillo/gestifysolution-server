package com.ventuit.adminstrativeapp.shared.validations.uniqueelements;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueElementsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueElements {
    String message() default "List must not contain duplicate values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
