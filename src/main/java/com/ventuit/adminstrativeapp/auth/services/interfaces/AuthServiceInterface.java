package com.ventuit.adminstrativeapp.auth.services.interfaces;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.auth.dto.LoginDto;
import com.ventuit.adminstrativeapp.auth.dto.LogoutDto;

public interface AuthServiceInterface {

    public ResponseEntity<Map<String, String>> login(LoginDto login);

    public ResponseEntity<Map<String, String>> logout(LogoutDto logout);

}
