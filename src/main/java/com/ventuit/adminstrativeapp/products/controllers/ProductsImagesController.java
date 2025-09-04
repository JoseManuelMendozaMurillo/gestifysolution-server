package com.ventuit.adminstrativeapp.products.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;
import com.ventuit.adminstrativeapp.products.services.ProductsImagesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ListProductsImagesDto> getById(@PathVariable Integer id) {
        ListProductsImagesDto productImage = this.productsImagesService.getById(id);
        if (productImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productImage);
    }

}
