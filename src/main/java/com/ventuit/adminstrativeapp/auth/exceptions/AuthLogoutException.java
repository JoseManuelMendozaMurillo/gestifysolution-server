package com.ventuit.adminstrativeapp.auth.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLogoutException extends RuntimeException {

    private int statusCode;

    public AuthLogoutException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
