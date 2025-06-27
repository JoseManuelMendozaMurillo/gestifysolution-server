package com.ventuit.adminstrativeapp.shared.validations.aspectratio;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AspectRatioValidator implements ConstraintValidator<AspectRatio, MultipartFile> {

    private String expectedRatio;
    private double tolerance;

    @Override
    public void initialize(AspectRatio constraintAnnotation) {
        this.expectedRatio = constraintAnnotation.value();
        this.tolerance = constraintAnnotation.tolerance();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Allow null/empty files, handle required validation separately
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return false; // Not a valid image
            }

            int width = image.getWidth();
            int height = image.getHeight();

            if (width <= 0 || height <= 0) {
                return false;
            }

            double actualRatio = (double) width / height;
            double expectedRatioValue = parseAspectRatio(expectedRatio);

            // Calculate tolerance range
            double minRatio = expectedRatioValue * (1 - tolerance / 100.0);
            double maxRatio = expectedRatioValue * (1 + tolerance / 100.0);

            return actualRatio >= minRatio && actualRatio <= maxRatio;

        } catch (IOException e) {
            return false; // Cannot read image
        }
    }

    private double parseAspectRatio(String ratio) {
        String[] parts = ratio.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid aspect ratio format. Expected format: width:height");
        }

        double width = Double.parseDouble(parts[0]);
        double height = Double.parseDouble(parts[1]);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Aspect ratio values must be positive");
        }

        return width / height;
    }
}