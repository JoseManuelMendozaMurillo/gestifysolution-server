package com.ventuit.adminstrativeapp.shared.validations.phone;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface Phone {
    String message() default "Invalid format. Use: +NN NNNNNNNNNN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
