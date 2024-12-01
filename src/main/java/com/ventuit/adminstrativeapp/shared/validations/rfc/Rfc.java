package com.ventuit.adminstrativeapp.shared.validations.rfc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(validatedBy = RfcValidator.class)
@Documented
public @interface Rfc {

    String message() default "Invalid RFC format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
