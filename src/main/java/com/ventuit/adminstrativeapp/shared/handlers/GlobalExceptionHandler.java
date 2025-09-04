package com.ventuit.adminstrativeapp.shared.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ventuit.adminstrativeapp.auth.exceptions.AuthClientErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthLogoutException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthRefreshTokenException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthServerErrorException;
import com.ventuit.adminstrativeapp.auth.exceptions.AuthUnauthorizedException;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserCreationException;
import com.ventuit.adminstrativeapp.keycloak.exceptions.KeycloakUserNotFoundException;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileGetException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileDeleteException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;
import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;
import com.ventuit.adminstrativeapp.utils.GlobalExceptionHandlerUtils;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // TODO Registering the error into the logs table in the database

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    /**
     * Handles exceptions thrown when a controller method argument (e.g., a path
     * variable)
     * cannot be converted to the required type.
     *
     * @param ex The exception thrown by Spring.
     * @return A ResponseEntity with a 400 Bad Request status and a structured error
     *         body.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        Object invalidValue = ex.getValue();
        String requiredType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();

        String detail = String.format("The parameter '%s' requires a valid '%s' but received '%s'.",
                parameterName, requiredType, invalidValue);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Parameter Type");
        problemDetail.setDetail(detail);

        Map<String, Object> properties = new HashMap<>();
        properties.put("parameter", parameterName);
        properties.put("invalidValue", invalidValue != null ? invalidValue.toString() : "null");
        properties.put("requiredType", requiredType);
        problemDetail.setProperties(properties);

        return ResponseEntity.badRequest().body(problemDetail);
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        HashMap<String, String> fieldsError = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // 2. Use the MessageSource to resolve the error code into a message
            String errorMessage = messageSource.getMessage(error, Locale.getDefault());
            fieldsError.put(error.getField(), errorMessage);
        });

        Map<String, Object> properties = new HashMap<>();
        properties.put("errors", GlobalExceptionHandlerUtils.formatToErrorFields(fieldsError));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("There are one or more invalid fields in the request.");
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

    // Handling file errors
    @ExceptionHandler(FileUploadException.class)
    @ResponseBody
    public ResponseEntity<String> handleFileUploadException(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(FileGetException.class)
    @ResponseBody
    public ResponseEntity<String> handleFileGetException(FileGetException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(FileDeleteException.class)
    @ResponseBody
    public ResponseEntity<String> handleFileDeleteException(FileDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    // Handling illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument: {}", ex.getMessage());
        logger.error("Stack trace: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred, please try again later.");
    }

    @ExceptionHandler(InvocationTargetException.class)
    @ResponseBody
    public ResponseEntity<String> handleInvocationTargetException(InvocationTargetException ex) {
        logger.error("Invocation target error: {}", ex.getMessage());
        logger.error("Stack trace: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred, please try again later.");
    }

    // Handling unexpected errors
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());
        logger.error("Stack trace: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred, please try again later.");
    }

}