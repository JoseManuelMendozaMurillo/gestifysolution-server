package com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastOrPresentDateValidator.class)
@Documented
public @interface PastOrPresentDate {
    String message() default "Date must be in the past or present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}