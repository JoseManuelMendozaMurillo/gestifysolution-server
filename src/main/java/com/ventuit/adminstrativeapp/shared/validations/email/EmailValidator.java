package com.ventuit.adminstrativeapp.shared.validations.email;

import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null)
            return true;
        return email.matches(RegexUtils.EMAIL_PATTERN);
    }

}
