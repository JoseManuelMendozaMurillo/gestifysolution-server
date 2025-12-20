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
public class TokenValidationDto {

    @NotBlank(message = "{Auth.TokenValidationDto.token.NotBlank}")
    private String token;

}
