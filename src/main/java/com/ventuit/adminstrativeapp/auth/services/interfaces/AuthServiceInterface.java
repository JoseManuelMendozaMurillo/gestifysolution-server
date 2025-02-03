package com.ventuit.adminstrativeapp.auth.services.interfaces;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.auth.dto.LoginDto;
import com.ventuit.adminstrativeapp.auth.dto.LogoutDto;
import com.ventuit.adminstrativeapp.auth.dto.RefreshTokenDto;
import com.ventuit.adminstrativeapp.auth.dto.TokenResponseDto;

public interface AuthServiceInterface {

    public TokenResponseDto login(LoginDto login);

    public ResponseEntity<Map<String, String>> logout(LogoutDto logout);

    public TokenResponseDto refreshToken(RefreshTokenDto refreshToken);

}
