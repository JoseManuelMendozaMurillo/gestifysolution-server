package com.ventuit.adminstrativeapp.keycloak.dto;

import com.ventuit.adminstrativeapp.keycloak.validations.uniqueemail.UniqueEmail;
import com.ventuit.adminstrativeapp.keycloak.validations.uniqueusername.UniqueUsername;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 255, message = "username must have a length between 3 and 255")
    @Pattern(regexp = RegexUtils.USERNAME_PATTERN, message = "username contains invalid character")
    @UniqueUsername()
    private String username;

    @NotBlank(message = "email cannot be empty")
    @Size(max = 255, message = "email cannot exceed 255 characters")
    @Email
    @UniqueEmail()
    private String email;

    @NotBlank(message = "first name cannot be empty")
    @Size(max = 255, message = "first name cannot exceed 255 characters")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "first name contains invalid character")
    private String firstName;

    @NotBlank(message = "last name cannot be empty")
    @Size(max = 255, message = "last name cannot exceed 255 characters")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "last name contains invalid character")
    private String lastName;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 255, message = "password must have a length between 8 and 255")
    private String password;

}
