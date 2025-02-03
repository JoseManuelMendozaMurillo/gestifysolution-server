package com.ventuit.adminstrativeapp.keycloak.mappers;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import com.ventuit.adminstrativeapp.keycloak.dto.ListKeycloakUser;

@Component
public class KeycloakUserMapper {

    public ListKeycloakUser toListKeycloakUser(UserRepresentation user) {
        return ListKeycloakUser.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .active(user.isEnabled())
                .build();
    }

}
