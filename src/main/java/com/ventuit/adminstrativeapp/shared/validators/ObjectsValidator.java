package com.ventuit.adminstrativeapp.shared.validators;

import java.util.HashMap;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Component
public class ObjectsValidator<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void validate(T classToValidate) throws ObjectNotValidException {
        // Method for validating fields from a class

        Set<ConstraintViolation<T>> violations = this.validator.validate(classToValidate);

        if (!violations.isEmpty()) {
            throw ObjectNotValidException
                    .builder()
                    .errorMessage("There are some errors in the request's fields")
                    .fieldsError(getFieldsErrorMap(violations))
                    .build();
        }

    }

    private HashMap<String, String> getFieldsErrorMap(Set<ConstraintViolation<T>> violations) {
        HashMap<String, String> fieldsError = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            String field = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldsError.put(field, errorMessage);
        }
        return fieldsError;
    }

}
