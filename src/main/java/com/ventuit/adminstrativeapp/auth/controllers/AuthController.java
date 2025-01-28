package com.ventuit.adminstrativeapp.auth.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.auth.dto.LoginDto;
import com.ventuit.adminstrativeapp.auth.dto.LogoutDto;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthLogoutException;
import com.ventuit.adminstrativeapp.auth.services.implementations.AuthServiceImpl;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        ResponseEntity<Map<String, String>> response = authService.login(login);
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto logout) {
        ResponseEntity<Map<String, String>> response = authService.logout(logout);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(204).build();
        } else {
            throw new AuthLogoutException(
                    "Logout failed: Unable to terminate the user's session. Please check the provided token and try again.",
                    response.getStatusCode().value());
        }
    }

}
