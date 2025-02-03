package com.ventuit.adminstrativeapp.shared.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ventuit.adminstrativeapp.auth.exceptions.AuthClientErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthLogoutException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthRefreshTokenException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthServerErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthUnauthorizedException;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserNotFoundException;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;
import com.ventuit.adminstrativeapp.utils.GlobalExceptionHandlerUtils;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // TODO Registering the error into the logs table in the database

    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleException(ObjectNotValidException ex) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(
                "errors",
                GlobalExceptionHandlerUtils.formatToErrorFields(ex.getFieldsError()));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid request");
        problemDetail.setDetail("There are one or more invalid fields in the request");
        problemDetail.setProperties(properties);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid request payload.");
        problemDetail.setDetail("There is an error in the structure of the request");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Error in the application");
        problemDetail.setDetail("An unexpected error has occurred in the application");
        return ResponseEntity.internalServerError().body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(400)
                .body(ex.getMessage());
    }

    // Handling keycloak errors
    @ExceptionHandler(KeycloakUserCreationException.class)
    @ResponseBody
    public ResponseEntity<String> handleKeycloakUserCreationException(KeycloakUserCreationException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getMessage());
    }

    @ExceptionHandler(KeycloakUserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleKeycloakUserNotFoundException(KeycloakUserNotFoundException ex) {
        return ResponseEntity.status(500)
                .body(ex.getMessage());
    }

    // Handling auth errors
    @ExceptionHandler(AuthUnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<String> handleAuthUnauthorizedException(AuthUnauthorizedException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(AuthClientErrorException.class)
    @ResponseBody
    public ResponseEntity<String> handleAuthClientErrorException(AuthClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(AuthServerErrorException.class)
    @ResponseBody
    public ResponseEntity<String> handleAuthServerErrorException(AuthServerErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(AuthRefreshTokenException.class)
    @ResponseBody
    public ResponseEntity<String> handleAuthRefreshTokenException(AuthRefreshTokenException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(AuthLogoutException.class)
    @ResponseBody
    public ResponseEntity<String> handleAuthLogoutException(AuthLogoutException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    // Handling unexpected errors
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

}