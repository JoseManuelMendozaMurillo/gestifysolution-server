package com.ventuit.adminstrativeapp.keycloak.dto;

import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.utils.RegexUtils;

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
public class UpdateKeycloakUser {

    @Size(max = 255, message = "{User.email.Size}")
    @Email
    private String email;

    @Size(max = 255, message = "{User.firstName.Size}")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "{User.firstName.Pattern}")
    private String firstName;

    @Size(max = 255, message = "{User.lastName.Size}")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "{User.lastName.Pattern}")
    private String lastName;
}
