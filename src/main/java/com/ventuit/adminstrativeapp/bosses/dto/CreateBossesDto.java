package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

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
    private String phone;

    @PastOrPresentDate
    private LocalDate birthdate;
}
