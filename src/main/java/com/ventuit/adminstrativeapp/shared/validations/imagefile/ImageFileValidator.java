package com.ventuit.adminstrativeapp.shared.validations.imagefile;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Allow null/empty files, handle required validation separately
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}