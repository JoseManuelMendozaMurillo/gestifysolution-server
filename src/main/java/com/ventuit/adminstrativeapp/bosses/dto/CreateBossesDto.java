package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBossesDto extends ExtendedBaseDto {

    @NotNull(message = "User information cannot be null")
    @Valid
    private CreateKeycloakUser user;

    @Phone
    @Unique(model = BossesModel.class, fieldName = "phone", message = "This phone is already registered")
    private String phone;

    @PastOrPresentDate
    private LocalDate birthdate;
}
