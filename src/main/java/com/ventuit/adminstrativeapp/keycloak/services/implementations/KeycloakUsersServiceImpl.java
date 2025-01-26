package com.ventuit.adminstrativeapp.keycloak.services.implementations;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;

import java.util.Collections;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.keycloak.dto.UpdateKeycloakUser;
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
        return keycloak.getAdminClient().users().get(userId).toRepresentation();
    }

    @Override
    public boolean deleteUserById(String id) {
        Response response = keycloak.getAdminClient().users().delete(id);
        return response.getStatusInfo().getFamily() == Family.SUCCESSFUL;
    }

    @Override
    public boolean updateUser(String userId, UpdateKeycloakUser user) {
        try {
            // Retrieve the UsersResource for the specified realm
            UsersResource usersResource = keycloak.getAdminClient().users();

            // Retrieve the UserResource for the specified userId
            UserResource userResource = usersResource.get(userId);

            // Fetch the existing UserRepresentation
            UserRepresentation userRepresentation = userResource.toRepresentation();

            // Update fields if they are provided in the UpdateKeycloakUser DTO
            if (user.getEmail() != null) {
                userRepresentation.setEmail(user.getEmail());
            }
            if (user.getFirstName() != null) {
                userRepresentation.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                userRepresentation.setLastName(user.getLastName());
            }

            // Update the user in Keycloak
            userResource.update(userRepresentation);

            return true; // Indicate success
        } catch (Exception e) {
            // Handle exceptions (e.g., user not found, Keycloak errors)
            e.printStackTrace();
            return false; // Indicate failure
        }
    }

}
