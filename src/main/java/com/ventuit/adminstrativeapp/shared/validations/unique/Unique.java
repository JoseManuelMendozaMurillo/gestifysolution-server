package com.ventuit.adminstrativeapp.shared.validations.unique;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
@Component
public @interface Unique {

    String message() default "The field is not unique";

    String fieldName();

    Class<?> model();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
