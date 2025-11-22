package com.ventuit.adminstrativeapp.products.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.services.ProductsService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("products")
public class ProductsController
        extends CrudControllerImpl<CreateProductDto, UpdateProductDto, ListProductDto, Integer, ProductsService> {

    public ProductsController(ProductsService service) {
        super(service);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListProductDto> create(@ModelAttribute CreateProductDto createDto) {
        ListProductDto entityCreated = this.service.create(createDto);
        return ResponseEntity.ok(entityCreated);
    }

    @Override
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListProductDto> update(@PathVariable Integer id,
            @ModelAttribute UpdateProductDto updateDto) {
        ListProductDto entityUpdated = this.service.update(id, updateDto);
        return ResponseEntity.ok(entityUpdated);
    }
}
