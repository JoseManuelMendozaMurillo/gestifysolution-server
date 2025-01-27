package com.ventuit.adminstrativeapp.auth.exceptions;

public class AuthUnauthorizedException extends RuntimeException {

    public AuthUnauthorizedException(String message) {
        super(message);
    }

}
