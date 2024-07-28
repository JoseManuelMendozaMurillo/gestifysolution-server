package com.ventuit.adminstrativeapp.bosses.dto;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BossesDto {
    private Integer id;

    private String keycloakUserId;

    @NotBlank(message = "You must send the bosses's name")
    private String name;

    @NotBlank(message = "You must send the bosses's surname")
    private String surname;

    private String phone;

    private Date birthdate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
