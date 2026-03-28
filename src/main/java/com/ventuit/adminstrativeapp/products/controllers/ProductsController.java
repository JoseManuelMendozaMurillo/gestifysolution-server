package com.ventuit.adminstrativeapp.products.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import com.ventuit.adminstrativeapp.products.controllers.interfaces.ProductsControllerInterface;
import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.ProductInterestDto;
import com.ventuit.adminstrativeapp.products.dto.ProductsSearchCriteria;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.services.ProductsService;
import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

@RestController
@RequestMapping("products")
public class ProductsController
        extends CrudControllerImpl<CreateProductDto, UpdateProductDto, ListProductDto, Integer, ProductsService>
        implements ProductsControllerInterface {

    public ProductsController(ProductsService service) {
        super(service);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<Page<ListProductDto>> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam Integer categoryId) {
        Page<ListProductDto> data = this.service.getAll(pageable, categoryId);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/get-all-active", params = "categoryId")
    public ResponseEntity<Page<ListProductDto>> getAllActive(@PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam Integer categoryId) {
        Page<ListProductDto> data = this.service.getAllActive(pageable, categoryId);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/get-all-inactive", params = "categoryId")
    public ResponseEntity<Page<ListProductDto>> getAllInactive(@PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam Integer categoryId) {
        Page<ListProductDto> data = this.service.getAllInactive(pageable, categoryId);
        return ResponseEntity.ok(data);
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

    @Override
    @GetMapping("/search")
    public ResponseEntity<Page<ListProductDto>> searchNotDeleted(
            @ModelAttribute ProductsSearchCriteria criteria,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.NOT_DELETED);
        Page<ListProductDto> data = this.service.searchProducts(criteria, pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    @GetMapping("/search/all")
    public ResponseEntity<Page<ListProductDto>> searchAll(
            @ModelAttribute ProductsSearchCriteria criteria,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.ALL);
        Page<ListProductDto> data = this.service.searchProducts(criteria, pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    @GetMapping("/search/deleted")
    public ResponseEntity<Page<ListProductDto>> searchDeleted(
            @ModelAttribute ProductsSearchCriteria criteria,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.DELETED);
        Page<ListProductDto> data = this.service.searchProducts(criteria, pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    @GetMapping("/{productId}/interest")
    public ResponseEntity<?> getProductInterest(@PathVariable Integer productId) {
        Optional<ProductInterestDto> result = this.service.getProductInterest(productId);
        return result.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
