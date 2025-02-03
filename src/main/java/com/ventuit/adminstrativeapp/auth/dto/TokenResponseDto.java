package com.ventuit.adminstrativeapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenResponseDto {
    private String access_token;

    private String refresh_token;

    private String token_type;

    private int expires_in;

    private int refresh_expires_in;

    private String session_state;

    private String scope;
}
