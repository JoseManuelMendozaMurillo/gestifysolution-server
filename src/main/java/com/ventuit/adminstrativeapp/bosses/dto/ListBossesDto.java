package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDate;
import java.util.Set;

import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.core.dto.BaseDto;
import com.ventuit.adminstrativeapp.keycloak.dto.ListKeycloakUser;

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
public class ListBossesDto extends BaseDto {

    private ListKeycloakUser user;

    private String phone;

    private LocalDate birthdate;

    private Set<BossesBusinessesModel> bossesBusinesses;
}
