package com.ventuit.adminstrativeapp.keycloak.services.interfaces;

import org.keycloak.representations.idm.UserRepresentation;

import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.dto.UpdateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;

public interface KeycloakUsersServiceInterface {

    UserRepresentation createUser(CreateKeycloakUser newUser) throws KeycloakUserCreationException;

    boolean updateUser(String userId, UpdateKeycloakUser user);

    UserRepresentation getUserById(String userId);

    boolean enabledUser(String userId);

    boolean disabledUser(String userId);

    boolean deleteUserById(String id);
}
