package com.ventuit.adminstrativeapp.products.dto;

import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class ProductsSearchCriteria {
    @Parameter(description = "Filter by part of the product name")
    private String name;
    
    @Parameter(description = "Filter by business ID")
    private Integer businessId;

    @Parameter(description = "Filter by branch ID")
    private Integer branchId;
    
    @Parameter(hidden = true)
    private DeletionStatus deletionStatus;
}
