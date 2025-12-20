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

    @NotBlank(message = "{User.username.NotBlank}")
    @Size(min = 3, max = 255, message = "{User.username.Size}")
    @Pattern(regexp = RegexUtils.USERNAME_PATTERN, message = "{User.username.Pattern}")
    @UniqueUsername()
    private String username;

    @NotBlank(message = "{User.email.NotBlank}")
    @Size(max = 255, message = "{User.email.Size}")
    @Email
    @UniqueEmail()
    private String email;

    @NotBlank(message = "{User.firstName.NotBlank}")
    @Size(max = 255, message = "{User.firstName.Size}")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "{User.firstName.Pattern}")
    private String firstName;

    @NotBlank(message = "{User.lastName.NotBlank}")
    @Size(max = 255, message = "{User.lastName.Size}")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "{User.lastName.Pattern}")
    private String lastName;

    @NotBlank(message = "{User.password.NotBlank}")
    @Size(min = 8, max = 100, message = "{User.password.Size}")
    private String password;

}
