package com.ventuit.adminstrativeapp.products.validations.isnotportraitimage;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validates that a product image identified by its ID is not the portrait
 * image.
 * If the image is the portrait, the validation fails.
 * Use on Integer or Long parameters representing the product image ID.
 */
@Documented
@Constraint(validatedBy = IsNotPortraitValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNotPortrait {
    String message() default "{ProductImage.id.IsNotPortrait}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}