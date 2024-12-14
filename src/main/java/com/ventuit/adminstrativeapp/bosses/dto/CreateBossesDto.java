package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;
import java.util.Set;

import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.shared.validations.pastorpresentdate.PastOrPresentDate;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBossesDto extends BaseDto {

    private String keycloakUserId;

    @NotBlank(message = "You must send the bosses's name")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "You must send the bosses's surname")
    @Size(max = 50, message = "Surname cannot exceed 50 characters")
    private String surname;

    @Phone
    private String phone;

    @PastOrPresentDate
    private LocalDate birthdate;

    private Set<BossesBusinessesModel> bossesBusinesses;
}
