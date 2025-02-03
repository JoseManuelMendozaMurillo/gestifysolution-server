package com.ventuit.adminstrativeapp.keycloak.exceptions;

public class KeycloakUserNotFoundException extends RuntimeException {

    public KeycloakUserNotFoundException(String username) {
        super("The user: " + username + " was not found in keycloak");
    }

}
