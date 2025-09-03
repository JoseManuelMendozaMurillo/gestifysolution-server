package com.ventuit.adminstrativeapp.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.shared.validations.aspectratio.AspectRatio;
import com.ventuit.adminstrativeapp.shared.validations.exist.Exist;
import com.ventuit.adminstrativeapp.shared.validations.imagefile.ImageFile;
import com.ventuit.adminstrativeapp.shared.validations.maxfilesize.MaxFileSize;
import com.ventuit.adminstrativeapp.shared.validations.validfile.ValidFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductsImagesDto {

    @NotNull(message = "{ProductImage.productId.NotNull}")
    @Min(value = 1, message = "{ProductImage.productId.Min}")
    @Exist(repository = ProductsRepository.class, method = "existsByIdAndDeletedAtIsNull", message = "{ProductImage.productId.Exist}", paramType = Integer.class)
    private Integer productId;

    @NotNull(message = "{Product.image.NotNull}")
    @ValidFile
    @ImageFile(message = "{Product.image.ImageFile}")
    @MaxFileSize(value = 2097152, message = "{Product.image.MaxFileSize}") // 2MB
    @AspectRatio(value = "1:1", tolerance = 10.0, message = "{Product.image.AspectRatio}")
    private MultipartFile image;

    @NotNull(message = "{Product.image.portrait.NotNull}")
    private Boolean portrait;

}
