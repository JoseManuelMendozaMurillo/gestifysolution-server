package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.core.dto.ExtendedBaseDto;
import com.ventuit.adminstrativeapp.keycloak.dto.UpdateKeycloakUser;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;

import jakarta.validation.Valid;
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
public class UpdateBossesDto extends ExtendedBaseDto {

    @Valid
    private UpdateKeycloakUser user;

    @Phone
    private String phone;

    @PastOrPresentDate
    private LocalDate birthdate;
}
