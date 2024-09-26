package com.ventuit.adminstrativeapp.bosses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.bosses.models.BossesModel;

@Repository
public interface BossesRepository extends JpaRepository<BossesModel, Integer> {
    BossesModel findByKeycloakUserId(String keycloakUserId);
}
