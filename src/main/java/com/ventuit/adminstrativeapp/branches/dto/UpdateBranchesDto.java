package com.ventuit.adminstrativeapp.branches.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

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
public class UpdateBranchesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @Unique(model = BranchesModel.class, fieldName = "name", message = "This branch is already registered")
    private String name;

    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    @Phone
    @Unique(model = BranchesModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @Size(max = 60, message = "Email cannot exceed 60 characters")
    @Email
    @Unique(model = BranchesModel.class, fieldName = "email", message = "This email is already registered")
    private String email;

    @PastOrPresentDate(message = "Opening date must be in the past or present")
    private LocalDate openingDate;

    @Valid
    private DirectionsDto direction;
}
