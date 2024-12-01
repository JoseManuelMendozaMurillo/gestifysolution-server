package com.ventuit.adminstrativeapp.shared.validations.phone;

import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null)
            return true;
        return phone.matches(RegexUtils.PHONE_NUMBER_PATTERN);
    }

}
