package com.ventuit.adminstrativeapp.utils;

public class ReflectionUtils {

    public static boolean hasField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                currentClass.getDeclaredField(fieldName);
                return true; // Field found
            } catch (NoSuchFieldException e) {
                // Field not found in current class; move to superclass
                currentClass = currentClass.getSuperclass();
            }
        }
        return false; // Field not found in class hierarchy
    }

}
