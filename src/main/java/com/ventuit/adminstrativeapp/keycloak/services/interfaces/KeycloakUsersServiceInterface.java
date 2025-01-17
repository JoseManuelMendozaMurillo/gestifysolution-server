package com.ventuit.adminstrativeapp.keycloak.services.interfaces;

import org.keycloak.representations.idm.UserRepresentation;

import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;

public interface KeycloakUsersServiceInterface {

    UserRepresentation createUser(CreateKeycloakUser newUser) throws KeycloakUserCreationException;

    UserRepresentation getUserById(String userId);

    boolean deleteUserById(String id);
}
