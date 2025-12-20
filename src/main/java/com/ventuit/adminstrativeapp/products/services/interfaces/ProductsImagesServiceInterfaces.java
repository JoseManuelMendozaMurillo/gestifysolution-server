package com.ventuit.adminstrativeapp.products.services.interfaces;

import java.util.List;

import com.ventuit.adminstrativeapp.products.dto.CreateProductsImagesDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductsImagesDto;

public interface ProductsImagesServiceInterfaces {

    public ListProductsImagesDto getById(Integer id);

    public List<ListProductsImagesDto> getByProductId(Integer id);

    public List<ListProductsImagesDto> getActiveByProductId(Integer id);

    public List<ListProductsImagesDto> getInactiveByProductId(Integer id);

    public ListProductsImagesDto create(CreateProductsImagesDto dto);

    public Boolean softDeleteById(Integer id);

    public Boolean deleteById(Integer id);

    public Boolean restoreById(Integer id);

    public ListProductsImagesDto setAsPortraitImage(Integer id);
}
