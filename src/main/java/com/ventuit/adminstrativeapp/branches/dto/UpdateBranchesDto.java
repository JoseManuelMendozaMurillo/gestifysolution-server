package com.ventuit.adminstrativeapp.branches.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

import jakarta.validation.Valid;
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
public class UpdateBranchesDto extends BaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    private String name;

    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    @Phone
    private String phone;

    @Size(max = 60, message = "Email cannot exceed 60 characters")
    @Email
    private String email;

    @PastOrPresentDate(message = "Opening date must be in the past or present")
    private LocalDate openingDate;

    @Valid
    private DirectionsDto direction;
}
