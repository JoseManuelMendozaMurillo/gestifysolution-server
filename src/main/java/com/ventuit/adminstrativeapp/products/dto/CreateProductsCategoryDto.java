package com.ventuit.adminstrativeapp.products.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.aspectratio.AspectRatio;
import com.ventuit.adminstrativeapp.shared.validations.imagefile.ImageFile;
import com.ventuit.adminstrativeapp.shared.validations.maxfilesize.MaxFileSize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductsCategoryDto extends ExtendedBaseDto {
    @NotBlank(message = "You must send the category name")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotNull(message = "You must send an image")
    @ImageFile(message = "Image must be an image file")
    @MaxFileSize(value = 2097152, message = "Image file size must not exceed 2MB") // 2MB
    @AspectRatio(value = "1:1", tolerance = 10.0, message = "Image must be square (1:1 aspect ratio) with 10% tolerance")
    private MultipartFile image;
}
