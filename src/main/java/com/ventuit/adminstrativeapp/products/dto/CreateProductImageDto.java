package com.ventuit.adminstrativeapp.products.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import com.ventuit.adminstrativeapp.shared.validations.aspectratio.AspectRatio;
import com.ventuit.adminstrativeapp.shared.validations.imagefile.ImageFile;
import com.ventuit.adminstrativeapp.shared.validations.maxfilesize.MaxFileSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductImageDto {

    @NotNull(message = "You must send an image file")
    @ImageFile(message = "Image must be an image file")
    @MaxFileSize(value = 2097152, message = "Image file size must not exceed 2MB") // 2MB
    @AspectRatio(value = "1:1", tolerance = 10.0, message = "Image must be square (1:1 aspect ratio) with 10% tolerance")
    private MultipartFile image;

    @NotNull(message = "You must specify if the image is portrait")
    private Boolean portrait;

}
