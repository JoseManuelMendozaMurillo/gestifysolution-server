package com.ventuit.adminstrativeapp.supplies.dto;

import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

import jakarta.validation.constraints.NotBlank;
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
public class CreateSuppliersDto extends ExtendedBaseDto {

    @NotBlank(message = "You must send the supplier's name")
    @Size(max = 60, message = "Name cannot exceed 60 characters")
    private String name;

    @Phone
    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    private String phone;

    @Email
    @Size(max = 60, message = "Email cannot exceed 60 characters")
    private String email;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

}
