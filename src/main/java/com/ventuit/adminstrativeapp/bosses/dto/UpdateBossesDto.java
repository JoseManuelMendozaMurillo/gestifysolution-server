package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;

import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.keycloak.dto.UpdateKeycloakUser;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
import com.ventuit.adminstrativeapp.shared.validations.phone.Phone;
import com.ventuit.adminstrativeapp.shared.validations.unique.Unique;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBossesDto {

    @Valid
    private UpdateKeycloakUser user;

    @Phone
    @Unique(model = BossesModel.class, fieldName = "phone", message = "{Boss.phone.Unique}")
    private String phone;

    @PastOrPresentDate
    private LocalDate birthdate;
}
