package com.ventuit.adminstrativeapp.keycloak.utils;

import java.util.List;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.keycloak.KeycloakProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakUtils {

    private final KeycloakProvider keycloak;

    public boolean isUsernameExists(String username) {
        UsersResource usersResource = keycloak.getAdminClient().users();
        List<UserRepresentation> users = usersResource.search(username, true);
        return !users.isEmpty();
    }

    public boolean isEmailExists(String email) {
        UsersResource usersResource = keycloak.getAdminClient().users();
        List<UserRepresentation> users = usersResource.searchByEmail(email, true);
        return !users.isEmpty();
    }

    public boolean isEmailExistsUpdate(String userId, String email) {
        UserRepresentation user = keycloak.getAdminClient().users().get(userId).toRepresentation();
        if (user.getEmail().equals(email))
            return false;

        UsersResource usersResource = keycloak.getAdminClient().users();
        List<UserRepresentation> users = usersResource.searchByEmail(email, true);
        return !users.isEmpty();
    }

}
