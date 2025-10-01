package com.ventuit.adminstrativeapp.products.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;
import com.ventuit.adminstrativeapp.products.services.ProductsImagesService;
import com.ventuit.adminstrativeapp.products.validations.isnotportraitimage.IsNotPortrait;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("products-images")
@RequiredArgsConstructor
public class ProductsImagesController {

    private final ProductsImagesService productsImagesService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListProductsImagesDto> create(@Valid @ModelAttribute CreateProductsImagesDto createDto) {
        ListProductsImagesDto entityCreated = this.productsImagesService.create(createDto);
        return ResponseEntity.ok(entityCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListProductsImagesDto> getById(@NotNull @PathVariable Integer id) {
        ListProductsImagesDto productImage = this.productsImagesService.getById(id);
        if (productImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productImage);
    }

    @GetMapping("/by-product/{id}")
    public ResponseEntity<List<ListProductsImagesDto>> getByProductId(@NotNull @PathVariable Integer id) {
        List<ListProductsImagesDto> productImages = this.productsImagesService.getByProductId(id);

        if (productImages == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productImages);
    }

    @GetMapping("/by-product/{id}/active")
    public ResponseEntity<List<ListProductsImagesDto>> getActiveByProductId(@NotNull @PathVariable Integer id) {
        List<ListProductsImagesDto> productImages = this.productsImagesService.getActiveByProductId(id);

        if (productImages == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productImages);
    }

    @GetMapping("/by-product/{id}/inactive")
    public ResponseEntity<List<ListProductsImagesDto>> getInactiveByProductId(@NotNull @PathVariable Integer id) {
        List<ListProductsImagesDto> productImages = this.productsImagesService.getInactiveByProductId(id);

        if (productImages == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productImages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@NotNull @IsNotPortrait @PathVariable Integer id) {
        Boolean isDeleted = this.productsImagesService.deleteById(id);

        if (isDeleted == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isDeleted) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Product image deleted successfully.");
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDelete(@NotNull @IsNotPortrait @PathVariable Integer id) {
        Boolean isDeleted = this.productsImagesService.softDeleteById(id);

        if (isDeleted == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isDeleted) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Product image soft deleted successfully.");
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<String> restore(@NotNull @PathVariable Integer id) {
        Boolean isRestored = this.productsImagesService.restoreById(id);

        if (isRestored == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isRestored) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Product image restored successfully.");
    }

    @PatchMapping("/set-as-portrait/{id}")
    public ResponseEntity<ListProductsImagesDto> setAsPortraitImage(@NotNull @PathVariable Integer id) {
        ListProductsImagesDto updatedImage = this.productsImagesService.setAsPortraitImage(id);
        if (updatedImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedImage);
    }
}
