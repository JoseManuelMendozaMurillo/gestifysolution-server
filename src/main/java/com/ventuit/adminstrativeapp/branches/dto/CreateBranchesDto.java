package com.ventuit.adminstrativeapp.branches.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.validations.email.Email;
import com.ventuit.adminstrativeapp.shared.validations.exist.Exist;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBranchesDto extends ExtendedBaseDto {

    @Size(max = 60, message = "{Branch.name.Size}")
    @NotBlank(message = "{Branch.name.NotBlank}")
    @Unique(model = BranchesModel.class, fieldName = "name", message = "{Branch.name.Unique}")
    private String name;

    @Size(max = 30, message = "{Branch.phone.Size}")
    @Phone
    @Unique(model = BranchesModel.class, fieldName = "phone", message = "{Branch.phone.Unique}")
    private String phone;

    @Size(max = 60, message = "{Branch.email.Size}")
    @Email
    @Unique(model = BranchesModel.class, fieldName = "email", message = "{Branch.email.Unique}")
    private String email;

    @PastOrPresentDate(message = "{Branch.openingDate.PastOrPresent}")
    private LocalDate openingDate;

    private boolean active;

    private Integer activeChangedBy;

    private LocalDateTime activeChangedAt;

    @NotNull(message = "{Branch.direction.NotNull}")
    @Valid
    private DirectionsDto direction;

    @NotNull(message = "{Branch.businessId.NotNull}")
    @Min(value = 1, message = "{Branch.businessId.Min}")
    @Exist(repository = BusinessesRepository.class, method = "existsByIdAndDeletedAtIsNull", message = "{Branch.businessId.Exist}", paramType = Integer.class)
    private Integer businessId;
}
