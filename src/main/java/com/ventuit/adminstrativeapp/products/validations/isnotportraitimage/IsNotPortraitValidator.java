package com.ventuit.adminstrativeapp.products.validations.isnotportraitimage;

import com.ventuit.adminstrativeapp.config.ContextProvider;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsImagesRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsNotPortraitValidator implements ConstraintValidator<IsNotPortrait, Integer> {

    private final ProductsImagesRepository productsImagesRepository = (ProductsImagesRepository) ContextProvider
            .getBean(ProductsImagesRepository.class);

    @Override
    public boolean isValid(Integer productImageId, ConstraintValidatorContext context) {
        // If the ID is null or not provided, we consider it valid to let other
        // validators (like @NotNull) handle it.
        if (productImageId == null) {
            return true;
        }

        // Find the product image by its ID
        ProductsImagesModel productImage = productsImagesRepository.findById(productImageId)
                .orElse(null);
        if (productImage == null) {
            // If the product image does not exist, we consider it valid to let other
            // validators handle it.
            return true;
        }
        // Return true if the image is not a portrait, false otherwise
        return !Boolean.TRUE.equals(productImage.isPortrait());
    }
}
