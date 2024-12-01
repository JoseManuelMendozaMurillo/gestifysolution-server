package com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PastOrPresentDateValidator implements ConstraintValidator<PastOrPresentDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Allow null values if required. Adjust as necessary.
        }
        return !date.isAfter(LocalDate.now());
    }

}
