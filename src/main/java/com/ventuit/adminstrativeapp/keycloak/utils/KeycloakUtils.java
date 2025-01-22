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
        System.out.println("Juan");
        return !users.isEmpty();
    }

}
