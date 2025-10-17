package com.ventuit.adminstrativeapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogoutDto {

    @NotBlank(message = "{Auth.LogoutDto.refreshToken.NotBlank}")
    private String refreshToken;

}
