package com.ventuit.adminstrativeapp.businesses.dto;

import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class BusinessesSearchCriteria {
    @Parameter(description = "Filter by part of the business name")
    private String name;
    
    @Parameter(hidden = true)
    private DeletionStatus deletionStatus;
}
