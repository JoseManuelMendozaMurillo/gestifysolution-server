package com.ventuit.adminstrativeapp.shared.dto;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectionsDto extends ExtendedBaseDto {

    @NotBlank(message = "{Direction.latitude.NotBlank}")
    @Pattern(regexp = RegexUtils.LATITUDE_PATTERN, message = "{Direction.latitude.Pattern}")
    @Size(max = 60, message = "{Direction.latitude.Size}")
    private String latitude;

    @NotBlank(message = "{Direction.longitude.NotBlank}")
    @Pattern(regexp = RegexUtils.LONGITUDE_PATTERN, message = "{Direction.longitude.Pattern}")
    @Size(max = 60, message = "{Direction.longitude.Size}")
    private String longitude;

    @NotBlank(message = "{Direction.street.NotBlank}")
    @Size(max = 60, message = "{Direction.street.Size}")
    private String street;

    @NotBlank(message = "{Direction.exteriorNumber.NotBlank}")
    @Pattern(regexp = RegexUtils.NUMBERS_ONLY_PATTERN, message = "{Direction.exteriorNumber.Pattern}")
    @Size(max = 10, message = "{Direction.exteriorNumber.Size}")
    private String exteriorNumber;

    @Pattern(regexp = RegexUtils.CAPITAL_LETTERS_ONLY_PATTERN, message = "{Direction.exteriorLetter.Pattern}")
    @Size(max = 10, message = "{Direction.exteriorLetter.Size}")
    private String exteriorLetter;

    @Pattern(regexp = RegexUtils.NUMBERS_ONLY_PATTERN, message = "{Direction.interiorNumber.Pattern}")
    @Size(max = 10, message = "{Direction.interiorNumber.Size}")
    private String interiorNumber;

    @Pattern(regexp = RegexUtils.CAPITAL_LETTERS_ONLY_PATTERN, message = "{Direction.interiorLetter.Pattern}")
    @Size(max = 10, message = "{Direction.interiorLetter.Size}")
    private String interiorLetter;

    @Size(max = 60, message = "{Direction.neighborhood.Size}")
    private String neighborhood;

    @NotBlank(message = "{Direction.postalCode.NotBlank}")
    @Pattern(regexp = RegexUtils.POSTAL_CODE_PATTERN, message = "{Direction.postalCode.Pattern}")
    @Size(max = 20, message = "{Direction.postalCode.Size}")
    private String postalCode;

    @NotBlank(message = "{Direction.city.NotBlank}")
    @Size(max = 60, message = "{Direction.city.Size}")
    private String city;

    @NotBlank(message = "{Direction.state.NotBlank}")
    @Size(max = 60, message = "{Direction.state.Size}")
    private String state;

    @NotBlank(message = "{Direction.country.NotBlank}")
    @Size(max = 60, message = "{Direction.country.Size}")
    private String country;
}
