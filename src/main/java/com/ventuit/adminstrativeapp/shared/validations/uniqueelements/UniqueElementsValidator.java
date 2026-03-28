package com.ventuit.adminstrativeapp.shared.validations.uniqueelements;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueElementsValidator implements ConstraintValidator<UniqueElements, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        // null collections are handled by @NotNull / @NotEmpty — skip here
        if (value == null) {
            return true;
        }

        Set<Object> seen = new HashSet<>();
        for (Object element : value) {
            if (!seen.add(element)) {
                // duplicate found
                return false;
            }
        }
        return true;
    }
}
