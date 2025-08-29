package com.ventuit.adminstrativeapp.shared.validations.validfile;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            // This case is handled by @NotNull and @NotEmpty, but as a safeguard
            return false;
        }

        if (file.getContentType() == null || file.getContentType().trim().isEmpty()) {
            // This is the main check for the content type
            return false;
        }

        return true;
    }
}
