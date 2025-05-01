package com.ventuit.adminstrativeapp.bosses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckPhoneExistDto {
    private String phone;
}
