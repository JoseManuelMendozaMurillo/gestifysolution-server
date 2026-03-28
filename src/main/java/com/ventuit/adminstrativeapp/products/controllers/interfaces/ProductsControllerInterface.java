package com.ventuit.adminstrativeapp.products.controllers.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.ProductsSearchCriteria;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.core.controllers.interfaces.CrudControllerInterface;

public interface ProductsControllerInterface extends CrudControllerInterface<CreateProductDto, UpdateProductDto, ListProductDto, Integer> {

    @GetMapping("/search")
    ResponseEntity<Page<ListProductDto>> searchNotDeleted(@ModelAttribute ProductsSearchCriteria criteria, Pageable pageable);

    @GetMapping("/search/all")
    ResponseEntity<Page<ListProductDto>> searchAll(@ModelAttribute ProductsSearchCriteria criteria, Pageable pageable);

    @GetMapping("/search/deleted")
    ResponseEntity<Page<ListProductDto>> searchDeleted(@ModelAttribute ProductsSearchCriteria criteria, Pageable pageable);

    @GetMapping("/{productId}/interest")
    ResponseEntity<?> getProductInterest(@PathVariable Integer productId);
}