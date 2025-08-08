package com.ventuit.adminstrativeapp.products.validations.onlyoneportraitimage;

import com.ventuit.adminstrativeapp.products.dto.CreateProductImageDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class OnlyOnePortraitImageValidator
        implements ConstraintValidator<OnlyOnePortraitImage, List<CreateProductImageDto>> {
    @Override
    public boolean isValid(List<CreateProductImageDto> value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        long count = value.stream().filter(img -> Boolean.TRUE.equals(img.getPortrait())).count();
        return count == 1;
    }
}
