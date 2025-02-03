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

    @Size(max = 255, message = "email cannot exceed 255 characters")
    @Email
    private String email;

    @Size(max = 255, message = "first name cannot exceed 255 characters")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "first name contains invalid character")
    private String firstName;

    @Size(max = 255, message = "last name cannot exceed 255 characters")
    @Pattern(regexp = RegexUtils.NAME_PATTERN, message = "last name contains invalid character")
    private String lastName;
}
