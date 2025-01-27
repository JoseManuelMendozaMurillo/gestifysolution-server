package com.ventuit.adminstrativeapp.auth.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthServerErrorException extends RuntimeException {

    private int statusCode;

    private String message;

    public AuthServerErrorException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

}
