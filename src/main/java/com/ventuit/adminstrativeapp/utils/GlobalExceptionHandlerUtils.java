package com.ventuit.adminstrativeapp.utils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalExceptionHandlerUtils {

    public static List<HashMap<String, String>> formatToErrorFields(HashMap<String, String> errorFields) {
        return errorFields.keySet().stream()
                .map(errorField -> new HashMap<String, String>() {
                    {
                        put("detail", errorFields.get(errorField));
                        put("pointer", "#/" + errorField.replace(".", "/"));
                    }
                })
                .collect(Collectors.toList());
    }

}
