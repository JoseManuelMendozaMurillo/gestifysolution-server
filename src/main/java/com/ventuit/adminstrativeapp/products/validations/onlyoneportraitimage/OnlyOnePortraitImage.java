package com.ventuit.adminstrativeapp.products.validations.onlyoneportraitimage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyOnePortraitImageValidator.class)
@Documented
public @interface OnlyOnePortraitImage {
    String message() default "Only one image can have portrait set to true";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
