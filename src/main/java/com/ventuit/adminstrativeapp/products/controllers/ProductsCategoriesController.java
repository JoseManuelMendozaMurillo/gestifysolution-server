package com.ventuit.adminstrativeapp.products.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.services.ProductsCategoriesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("products-categories")
public class ProductsCategoriesController extends
        CrudControllerImpl<CreateProductsCategoryDto, UpdateProductsCategoryDto, ListProductsCategoryDto, Integer, ProductsCategoriesService> {

    public ProductsCategoriesController(ProductsCategoriesService crudService) {
        super(crudService);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListProductsCategoryDto> create(@ModelAttribute CreateProductsCategoryDto createDto) {
        ListProductsCategoryDto entityCreated = this.crudService.create(createDto);
        return ResponseEntity.ok(entityCreated);
    }

    @Override
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListProductsCategoryDto> update(@PathVariable Integer id,
            @ModelAttribute UpdateProductsCategoryDto updateDto) {
        ListProductsCategoryDto entityUpdated = this.crudService.update(id, updateDto);
        return ResponseEntity.ok(entityUpdated);
    }
}
