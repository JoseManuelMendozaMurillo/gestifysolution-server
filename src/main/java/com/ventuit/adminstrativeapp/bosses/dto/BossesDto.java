package com.ventuit.adminstrativeapp.bosses.dto;

import java.util.Date;
import java.util.Set;

import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.core.dto.BaseDto;

import jakarta.validation.constraints.NotBlank;
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
public class BossesDto extends BaseDto {

    private String keycloakUserId;

    @NotBlank(message = "You must send the bosses's name")
    private String name;

    @NotBlank(message = "You must send the bosses's surname")
    private String surname;

    private String phone;

    private Date birthdate;

    private Set<BossesBusinessesModel> bossesBusinesses;
}
