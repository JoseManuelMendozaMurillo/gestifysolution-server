package com.ventuit.adminstrativeapp.bosses.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;

public interface BossesServiceInterface {

    public Boolean isPhoneExists(String phone);

    public Page<ListBusinessesDto> getBossesBusinesses(String bossIdOrUsername, Pageable pageable);

}
