package com.ventuit.adminstrativeapp.auth.dto;

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
public class LoginDto {

    @NotBlank(message = "username cannot be empty")
    @Size(min = 3, max = 255, message = "username must have a length between 3 and 255")
    @Pattern(regexp = RegexUtils.USERNAME_PATTERN, message = "username contains invalid character")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 255, message = "password must have a length between 8 and 255")
    private String password;

}
