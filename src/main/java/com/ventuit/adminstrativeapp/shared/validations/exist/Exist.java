package com.ventuit.adminstrativeapp.shared.validations.exist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exist {
    String message();

    /**
     * The repository class to use for validation
     * Must be annotated with @Repository or @Component
     */
    Class<?> repository();

    /**
     * 
     * @return
     */
    String method();

    /**
     * 
     * @return
     */
    Class<?> paramType();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
