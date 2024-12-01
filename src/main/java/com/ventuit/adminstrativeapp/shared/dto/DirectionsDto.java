package com.ventuit.adminstrativeapp.shared.dto;

import com.ventuit.adminstrativeapp.utils.RegexUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectionsDto {

    @NotBlank(message = "Latitude is required")
    @Pattern(regexp = RegexUtils.LATITUDE_PATTERN, message = "Invalid latitude")
    @Size(max = 60, message = "Latitude cannot exceed 60 characters")
    private String latitude;

    @NotBlank(message = "Longitude is required")
    @Pattern(regexp = RegexUtils.LONGITUDE_PATTERN, message = "Invalid longitude")
    @Size(max = 60, message = "Longitude cannot exceed 60 characters")
    private String longitude;

    @NotBlank(message = "Street is required")
    @Size(max = 60, message = "Street cannot exceed 60 characters")
    private String street;

    @NotBlank(message = "Street is required")
    @Pattern(regexp = RegexUtils.NUMBERS_ONLY_PATTERN, message = "Exterior number must contain only numbers")
    @Size(max = 10, message = "Exterior number cannot exceed 10 characters")
    private String exteriorNumber;

    @Pattern(regexp = RegexUtils.CAPITAL_LETTERS_ONLY_PATTERN, message = "Exterior letter must contain only capital letters")
    @Size(max = 10, message = "Exterior letter cannot exceed 10 characters")
    private String exteriorLetter;

    @Pattern(regexp = RegexUtils.NUMBERS_ONLY_PATTERN, message = "Interior number must contain only numbers")
    @Size(max = 10, message = "Interior number cannot exceed 10 characters")
    private String interiorNumber;

    @Pattern(regexp = RegexUtils.CAPITAL_LETTERS_ONLY_PATTERN, message = "Interior letter must contain only capital letters")
    @Size(max = 10, message = "Interior letter cannot exceed 10 characters")
    private String interiorLetter;

    @Size(max = 60, message = "Neighborhood cannot exceed 60 characters")
    private String neighborhood;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = RegexUtils.POSTAL_CODE_PATTERN, message = "Postal code must contain five numbers")
    @Size(max = 20, message = "Postal code cannot exceed 20 numbers")
    private String postalCode;

    @NotBlank(message = "City is required")
    @Size(max = 60, message = "City cannot exceed 60 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 60, message = "State cannot exceed 60 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 60, message = "Country cannot exceed 60 characters")
    private String country;
}
