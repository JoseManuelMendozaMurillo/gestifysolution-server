package com.ventuit.adminstrativeapp.keycloak.exceptions;

import lombok.Getter;

@Getter
public class KeycloakUserCreationException extends RuntimeException {

    private int statusCode;

    public KeycloakUserCreationException(String errorMessage) {
        super("Failed to create user: " + errorMessage);
        this.statusCode = 500;
    }

    public KeycloakUserCreationException(String errorMessage, int statusCode) {
        super("Failed to create user: " + errorMessage);
        this.statusCode = statusCode;
    }
}