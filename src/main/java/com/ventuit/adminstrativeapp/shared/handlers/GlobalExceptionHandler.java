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

import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;
import com.ventuit.adminstrativeapp.utils.GlobalExceptionHandlerUtils;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

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
        // TODO Registering the error into the logs table in the database

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Error in the application");
        problemDetail.setDetail("An unexpected error has occurred in the application");
        return ResponseEntity.internalServerError().body(problemDetail);
    }

}