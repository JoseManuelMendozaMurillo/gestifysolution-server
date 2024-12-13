package com.ventuit.adminstrativeapp.branches.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

import jakarta.validation.Valid;
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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "Name cannot exceed 60 characters")
    @NotBlank(message = "You must send the branches's name")
    private String name;

    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    @Phone
    private String phone;

    @Size(max = 60, message = "Email cannot exceed 60 characters")
    @Email
    private String email;

    @PastOrPresentDate(message = "Opening date must be in the past or present")
    private LocalDate openingDate;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @NotNull(message = "You must send the direction's branch information")
    @Valid
    private DirectionsDto direction;
}
