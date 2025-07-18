package com.ventuit.adminstrativeapp.businesses.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.serialization.BusinessesTypeModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.IndustriesModelDeserializer;
import com.ventuit.adminstrativeapp.businesses.serialization.TypesRegimensTaxesModelDeserializer;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.aspectratio.AspectRatio;
import com.ventuit.adminstrativeapp.shared.validations.imagefile.ImageFile;
import com.ventuit.adminstrativeapp.shared.validations.maxfilesize.MaxFileSize;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.rfc.Rfc;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.validation.constraints.Size;
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
public class UpdateBusinessesDto extends ExtendedBaseDto {

    @Size(max = 50, message = "Name cannot exceed 50 characters")
    @Unique(model = BusinessesModel.class, fieldName = "name", message = "This business is already registered")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters long")
    @Rfc
    @Unique(model = BusinessesModel.class, fieldName = "rfc", message = "This rfc is already registered")
    private String rfc;

    @PastOrPresentDate(message = "Establishment date must be in the past or present")
    private LocalDate establishmentDate;

    @JsonDeserialize(using = IndustriesModelDeserializer.class)
    private IndustriesModel industry;

    @JsonDeserialize(using = BusinessesTypeModelDeserializer.class)
    private BusinessesTypeModel businessesType;

    @JsonDeserialize(using = TypesRegimensTaxesModelDeserializer.class)
    private TypesRegimensTaxesModel taxRegimen;

    @ImageFile(message = "Logo must be an image file")
    @MaxFileSize(value = 2097152, message = "Logo file size must not exceed 2MB") // 2MB
    @AspectRatio(value = "1:1", tolerance = 10.0, message = "Logo must be square (1:1 aspect ratio) with 10% tolerance")
    private MultipartFile logo;
}
