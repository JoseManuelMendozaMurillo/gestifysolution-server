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

    @NotBlank(message = "{Auth.LoginDto.username.NotBlank}")
    @Size(min = 3, max = 255, message = "{Auth.LoginDto.username.Size}")
    @Pattern(regexp = RegexUtils.USERNAME_PATTERN, message = "{Auth.LoginDto.username.Pattern}")
    private String username;

    @NotBlank(message = "{Auth.LoginDto.password.NotBlank}")
    @Size(min = 8, max = 255, message = "{Auth.LoginDto.password.Size}")
    private String password;

}
