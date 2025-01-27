package com.ventuit.adminstrativeapp.auth.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthClientErrorException extends RuntimeException {

    private int statusCode;

    private String message;

    public AuthClientErrorException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

}
