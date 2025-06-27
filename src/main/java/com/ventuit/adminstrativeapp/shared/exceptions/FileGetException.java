package com.ventuit.adminstrativeapp.shared.exceptions;

public class FileGetException extends RuntimeException {
    public FileGetException(String message) {
        super(message);
    }

    public FileGetException(String message, Throwable cause) {
        super(message, cause);
    }
}