package com.ventuit.adminstrativeapp.products.dto;

import java.util.List;

import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.products.validations.atmostoneportraitimage.AtMostOnePortraitImage;
import com.ventuit.adminstrativeapp.shared.validations.exist.Exist;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;
import com.ventuit.adminstrativeapp.shared.validations.uniqueelements.UniqueElements;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProductDto {

    @Size(max = 100, message = "{Product.name.Size}")
    @Unique(model = ProductsModel.class, fieldName = "name", message = "{Product.name.Unique}")
    private String name;

    @Size(max = 500, message = "{Product.description.Size}")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "{Product.price.DecimalMin}")
    @Digits(integer = 10, fraction = 2, message = "{Product.price.Digits}")
    private Double price;

    private Boolean active;

    @Min(value = 1, message = "{Product.categoryId.Min}")
    @Exist(repository = ProductsCategoriesRepository.class, method = "existsByIdAndDeletedAtIsNull", message = "{Product.categoryId.Exist}", paramType = Integer.class)
    private Integer categoryId;

    @Size(min = 1, message = "{Product.branchIds.NotEmpty}")
    @UniqueElements(message = "{Product.branchIds.UniqueElements}")
    @Exist(repository = BranchesRepository.class, method = "existsAllByIdIn", message = "{Product.branchIds.Exist}", paramType = List.class)
    private List<Integer> branchIds;

    @Valid
    @AtMostOnePortraitImage(message = "{Product.images.AtMostOnePortraitImage}")
    private List<CreateProductImageDto> images;

}
