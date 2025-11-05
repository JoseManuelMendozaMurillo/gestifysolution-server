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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateBusinessesDto extends ExtendedBaseDto {

    @NotBlank(message = "{Business.name.NotBlank}")
    @Size(max = 50, message = "{Business.name.Size}")
    @Unique(model = BusinessesModel.class, fieldName = "name", message = "{Business.name.Unique}")
    private String name;

    @Size(max = 200, message = "{Business.description.Size}")
    private String description;

    @NotBlank(message = "{Business.rfc.NotBlank}")
    @Size(min = 12, max = 13, message = "{Business.rfc.Size}")
    @Rfc(message = "{Business.rfc.Rfc}")
    @Unique(model = BusinessesModel.class, fieldName = "rfc", message = "{Business.rfc.Unique}")
    private String rfc;

    @PastOrPresentDate(message = "{Business.establishmentDate.PastOrPresent}")
    private LocalDate establishmentDate;

    @NotNull(message = "{Business.industry.NotNull}")
    @JsonDeserialize(using = IndustriesModelDeserializer.class)
    private IndustriesModel industry;

    @NotNull(message = "{Business.businessType.NotNull}")
    @JsonDeserialize(using = BusinessesTypeModelDeserializer.class)
    private BusinessesTypeModel businessesType;

    @NotNull(message = "{Business.taxRegimen.NotNull}")
    @JsonDeserialize(using = TypesRegimensTaxesModelDeserializer.class)
    private TypesRegimensTaxesModel taxRegimen;

    /**
     * Logo image file for the business
     */
    @ImageFile(message = "{Business.logo.ImageFile}")
    @MaxFileSize(value = 2097152, message = "{Business.logo.MaxFileSize}") // 2MB
    @AspectRatio(value = "1:1", tolerance = 10.0, message = "{Business.logo.AspectRatio}")
    private MultipartFile logo;

}
