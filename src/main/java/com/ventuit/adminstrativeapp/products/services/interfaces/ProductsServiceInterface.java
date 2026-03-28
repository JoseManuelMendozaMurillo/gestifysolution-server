package com.ventuit.adminstrativeapp.products.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.ProductsSearchCriteria;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;
import com.ventuit.adminstrativeapp.products.dto.ProductInterestDto;

import java.util.Optional;

public interface ProductsServiceInterface
        extends CrudServiceInterface<CreateProductDto, UpdateProductDto, ListProductDto, Integer> {
    Page<ListProductDto> searchProducts(ProductsSearchCriteria criteria, Pageable pageable);

    Optional<ProductInterestDto> getProductInterest(Integer productId);
}
