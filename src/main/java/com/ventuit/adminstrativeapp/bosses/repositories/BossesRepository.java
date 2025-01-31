package com.ventuit.adminstrativeapp.bosses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface BossesRepository extends BaseRepository<BossesModel, Integer> {
    BossesModel findByKeycloakUsername(String keycloakUsername);
}
