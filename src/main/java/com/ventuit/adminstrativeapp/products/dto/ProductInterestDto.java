package com.ventuit.adminstrativeapp.products.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductInterestDto {
    private String message;
    private List<Integer> branchIds;
}
