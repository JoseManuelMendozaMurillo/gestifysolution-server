package com.ventuit.adminstrativeapp.shared.validations.aspectratio;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AspectRatioValidator.class)
@Documented
public @interface AspectRatio {
    String message() default "Image aspect ratio is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Expected aspect ratio (width:height)
     * Examples: "16:9", "4:3", "1:1"
     */
    String value() default "1:1";

    /**
     * Tolerance for aspect ratio validation (percentage)
     * Default 5% tolerance
     */
    double tolerance() default 5.0;
}