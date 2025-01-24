package com.ventuit.adminstrativeapp.keycloak.validations.uniqueemail;

import com.ventuit.adminstrativeapp.config.ContextProvider;
import com.ventuit.adminstrativeapp.keycloak.utils.KeycloakUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private KeycloakUtils keycloakUtils;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        this.keycloakUtils = (KeycloakUtils) ContextProvider.getBean(KeycloakUtils.class);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // or false, depending on your validation requirements
        }
        return !this.keycloakUtils.isEmailExists(email);
    }

}
