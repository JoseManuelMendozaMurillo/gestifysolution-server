package com.ventuit.adminstrativeapp.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListKeycloakUser {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private boolean active;

}
