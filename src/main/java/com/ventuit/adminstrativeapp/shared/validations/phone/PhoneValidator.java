package com.ventuit.adminstrativeapp.shared.validations.phone;

import java.util.HashSet;
import java.util.Set;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private static final Set<Integer> validCountryCodes = new HashSet<>();

    // Precompute valid country codes (runs once at class load)
    static {
        for (String region : phoneUtil.getSupportedRegions()) {
            int countryCode = phoneUtil.getCountryCodeForRegion(region);
            if (countryCode > 0) {
                validCountryCodes.add(countryCode);
            }
        }
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null)
            return true; // Adjust null handling as needed

        // ----------------------------------
        // 1. Validate against Regex Pattern
        // ----------------------------------
        if (!phone.matches(RegexUtils.PHONE_NUMBER_PATTERN)) {
            setCustomMessage(context, "Invalid format: Must be +[country code (1,3 numbers)] [(4,14) numbers]");
            return false;
        }

        try {
            // ----------------------------------
            // 2. Parse the phone number
            // ----------------------------------
            PhoneNumber parsedNumber = phoneUtil.parse(phone, "ZZ");

            // ----------------------------------
            // 3. Validate Country Code
            // ----------------------------------
            // Extract country code directly from the regex-validated string
            String[] parts = phone.split(" ", 2);
            String countryCodeStr = parts[0].substring(1); // Remove the "+"
            int countryCode;
            try {
                countryCode = Integer.parseInt(countryCodeStr);
            } catch (NumberFormatException e) {
                // Regex ensures this is numeric, so this block is a safeguard
                setCustomMessage(context, "Invalid country code format");
                return false;
            }
            // Make the validation
            if (!validCountryCodes.contains(countryCode)) {
                setCustomMessage(context, "Invalid country code: +" + countryCode);
                return false;
            }

            // ----------------------------------
            // 4. Validate Full Phone Number
            // ----------------------------------
            if (!phoneUtil.isValidNumber(parsedNumber)) {
                setCustomMessage(context, "Invalid phone number for country code +" + countryCode);
                return false;
            }

            return true;

        } catch (NumberParseException e) {
            // Fallback for parsing failures (shouldn't happen if regex passed)
            setCustomMessage(context, "Invalid phone number format");
            return false;
        }
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}
