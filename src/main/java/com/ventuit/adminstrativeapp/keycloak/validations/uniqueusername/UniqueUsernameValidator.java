package com.ventuit.adminstrativeapp.keycloak.validations.uniqueusername;

import com.ventuit.adminstrativeapp.config.ContextProvider;
import com.ventuit.adminstrativeapp.keycloak.utils.KeycloakUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private KeycloakUtils keycloakUtils;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        this.keycloakUtils = (KeycloakUtils) ContextProvider.getBean(KeycloakUtils.class);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return true; // or false, depending on your validation requirements
        }
        return !this.keycloakUtils.isUsernameExists(username);
    }
}
