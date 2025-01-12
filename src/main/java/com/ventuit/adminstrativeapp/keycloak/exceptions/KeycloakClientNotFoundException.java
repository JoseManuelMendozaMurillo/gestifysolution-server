package com.ventuit.adminstrativeapp.keycloak.exceptions;

public class KeycloakClientNotFoundException extends RuntimeException {

    public KeycloakClientNotFoundException(String clientId, String realmName) {
        super("Keycloak client with " + clientId + " id was not found in the " + realmName + " realm");
    }
}
