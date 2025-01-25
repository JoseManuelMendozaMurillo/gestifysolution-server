package com.ventuit.adminstrativeapp.bosses.services;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.ListBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.mappers.BossesMapper;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.bosses.repositories.BossesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.keycloak.services.implementations.KeycloakUsersServiceImpl;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class BossesService
        extends
        CrudServiceImpl<CreateBossesDto, UpdateBossesDto, ListBossesDto, BossesModel, Integer, BossesMapper, BossesRepository> {

    @Autowired
    private KeycloakUsersServiceImpl usersService;

    public BossesService(BossesRepository repository, BossesMapper mapper) {
        super(repository, mapper);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void create(CreateBossesDto dto) {
        // Validate the dto
        this.creatingDtoValidator.validate(dto);

        // Create keycloak user
        UserRepresentation user = usersService.createUser(dto.getUser());

        // Create the record in the database
        try {
            BossesModel entity = this.mapper.toEntity(dto);
            entity.setKeycloakUserId(user.getId());
            this.repository.save(entity);
        } catch (Exception e) {
            usersService.deleteUserById(user.getId());
            throw e;
        }
    }

}
