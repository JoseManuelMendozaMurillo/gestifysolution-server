package com.ventuit.adminstrativeapp.shared.validations.rfc;

import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RfcValidator implements ConstraintValidator<Rfc, String> {

    @Override
    public boolean isValid(String rfc, ConstraintValidatorContext context) {
        if (rfc == null)
            return false;
        return rfc.matches(RegexUtils.RFC_PATTERN);
    }

}
