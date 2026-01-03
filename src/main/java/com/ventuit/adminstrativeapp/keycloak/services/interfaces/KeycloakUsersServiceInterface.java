package com.ventuit.adminstrativeapp.keycloak.services.interfaces;

import org.keycloak.representations.idm.UserRepresentation;

import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.dto.UpdateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;

public interface KeycloakUsersServiceInterface {

    UserRepresentation createUser(CreateKeycloakUser newUser) throws KeycloakUserCreationException;

    boolean updateUser(String username, UpdateKeycloakUser user);

    UserRepresentation getUserByUsername(String username);

    boolean enabledUserByUsername(String username);

    boolean disabledUserByUsername(String username);

    boolean deleteUserByUsername(String username);

    void deleteAllUsers();
}
