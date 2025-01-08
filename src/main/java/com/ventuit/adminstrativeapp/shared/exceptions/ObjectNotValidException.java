package com.ventuit.adminstrativeapp.shared.exceptions;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ObjectNotValidException extends RuntimeException {

    private final String errorMessage;

    private final HashMap<String, String> fieldsError;

}