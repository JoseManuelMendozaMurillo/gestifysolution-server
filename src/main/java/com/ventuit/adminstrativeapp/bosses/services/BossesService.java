package com.ventuit.adminstrativeapp.bosses.services;

import java.util.HashMap;
import java.util.Optional;

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
import com.ventuit.adminstrativeapp.keycloak.utils.KeycloakUtils;
import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class BossesService
        extends
        CrudServiceImpl<CreateBossesDto, UpdateBossesDto, ListBossesDto, BossesModel, Integer, BossesMapper, BossesRepository> {

    @Autowired
    private KeycloakUsersServiceImpl usersService;
    @Autowired
    private KeycloakUtils keycloakUtils;

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

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean update(Integer id, UpdateBossesDto dto) {
        // Validate the dto
        this.updatingDtoValidator.validate(dto);

        Optional<BossesModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return false;

        BossesModel existingEntity = optionalEntity.get();

        dto.setDeletedAt(existingEntity.getDeletedAt()); // preserve logical deletion when updating

        // Update keycloak user
        if (dto.getUser() != null) {
            String newEmail = dto.getUser().getEmail();

            // Validate unique email
            if (newEmail != null && keycloakUtils.isEmailExistsUpdate(existingEntity.getKeycloakUserId(), newEmail)) {
                HashMap<String, String> fieldsError = new HashMap<>() {
                    {
                        put("user/email", "The email already exist");
                    }
                };
                throw ObjectNotValidException
                        .builder()
                        .fieldsError(fieldsError)
                        .build();
            }

            boolean isKeycloakUserUpdated = usersService.updateUser(existingEntity.getKeycloakUserId(), dto.getUser());

            if (!isKeycloakUserUpdated)
                return false;
        }

        BossesModel updatedEntity = this.mapper.updateFromDto(dto, existingEntity);

        return updatedEntity != null; // Return true if the entity was updated successfully
    }

}
