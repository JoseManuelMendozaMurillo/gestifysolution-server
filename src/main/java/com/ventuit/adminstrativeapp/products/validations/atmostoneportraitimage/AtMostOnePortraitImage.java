package com.ventuit.adminstrativeapp.products.validations.atmostoneportraitimage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtMostOnePortraitImageValidator.class)
@Documented
public @interface AtMostOnePortraitImage {
    String message() default "{Product.images.AtMostOnePortraitImage}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
