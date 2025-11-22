package com.ventuit.adminstrativeapp.bosses.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.bosses.dto.CheckPhoneExistDto;
import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.ListBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.services.implementations.BossesServiceImpl;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;

@RestController
@RequestMapping("bosses")
public class BossesController
        extends CrudControllerImpl<CreateBossesDto, UpdateBossesDto, ListBossesDto, Integer, BossesServiceImpl> {

    public BossesController(BossesServiceImpl service) {
        super(service);
    }

    @PostMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExists(@RequestBody CheckPhoneExistDto phoneExistDto) {
        Boolean isPhoneExist = this.service.isPhoneExists(phoneExistDto.getPhone());
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", isPhoneExist);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bossIdOrUsername}/businesses")
    public ResponseEntity<Page<ListBusinessesDto>> getBossesWithBusinesses(
            @PathVariable String bossIdOrUsername,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<ListBusinessesDto> bossBusinesses = this.service.getBossesBusinesses(bossIdOrUsername, pageable);
        return ResponseEntity.ok(bossBusinesses);
    }

}
