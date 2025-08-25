package com.ventuit.adminstrativeapp.shared.validators;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Component
@RequiredArgsConstructor
public class ObjectsValidator<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final MessageSource messageSource;

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

            String defaultErrorMessage = messageSource.getMessage("default.error.message", null,
                    "Invalid value. Please check the format.", Locale.getDefault());

            // Original message, e.g., "{Exist.categoryId}"
            String messageWithBraces = violation.getMessage();

            // Remove the first '{' and last '}'
            // Add a check to prevent errors on very short or empty strings
            String errorMessageCode = messageWithBraces;
            if (messageWithBraces != null && messageWithBraces.length() > 2) {
                errorMessageCode = messageWithBraces.substring(1, messageWithBraces.length() - 1);
            }

            String errorMessage = messageSource.getMessage(errorMessageCode, null, defaultErrorMessage,
                    Locale.getDefault());
            fieldsError.put(field, errorMessage);
        }
        return fieldsError;
    }

}
