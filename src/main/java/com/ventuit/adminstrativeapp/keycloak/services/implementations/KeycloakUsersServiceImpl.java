package com.ventuit.adminstrativeapp.keycloak.services.implementations;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;

import java.util.Collections;

import org.keycloak.representations.idm.CredentialRepresentation;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;
import com.ventuit.adminstrativeapp.keycloak.services.interfaces.KeycloakUsersServiceInterface;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakUsersServiceImpl implements KeycloakUsersServiceInterface {

    private final KeycloakProvider keycloak;

    @Override
    public UserRepresentation createUser(CreateKeycloakUser newUser) throws KeycloakUserCreationException {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setEmailVerified(false);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setValue(newUser.getPassword());
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        user.setCredentials(Collections.singletonList(credentials));

        Response response = keycloak.getAdminClient().users().create(user);

        // Check if the user was created
        if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            // User created successfully
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            return keycloak.getAdminClient().users().get(userId).toRepresentation();
        } else {
            // Handle errors
            String errorMessage = response.readEntity(String.class);
            throw new KeycloakUserCreationException(errorMessage, response.getStatus());
        }

    }

    @Override
    public UserRepresentation getUserById(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public boolean deleteUserById(String id) {
        Response response = keycloak.getAdminClient().users().delete(id);
        return response.getStatusInfo().getFamily() == Family.SUCCESSFUL;
    }

}
