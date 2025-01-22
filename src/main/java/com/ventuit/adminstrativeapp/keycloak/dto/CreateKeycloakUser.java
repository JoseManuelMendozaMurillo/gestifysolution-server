package com.ventuit.adminstrativeapp.keycloak.dto;

import com.ventuit.adminstrativeapp.keycloak.validations.uniqueusername.UniqueUsername;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateKeycloakUser {

    @NotBlank(message = "username cannot be empty")
    @UniqueUsername()
    private String username;

    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "first name cannot be empty")
    private String firstName;

    @NotBlank(message = "last name cannot be empty")
    private String lastName;

    @NotBlank(message = "password cannot be empty")
    private String password;

}
