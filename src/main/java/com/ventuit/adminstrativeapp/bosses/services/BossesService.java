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
            entity.setKeycloakUsername(user.getUsername());
            entity.setCreatedBy(this.getUsername());
            this.repository.save(entity);
        } catch (Exception e) {
            usersService.deleteUserByUsername(user.getUsername());
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

        dto.setUpdatedBy(this.getUsername()); // preserve logical deletion when updating

        // Update keycloak user
        if (dto.getUser() != null) {
            String newEmail = dto.getUser().getEmail();

            // Validate unique email
            if (newEmail != null && keycloakUtils.isEmailExistsUpdate(existingEntity.getKeycloakUsername(), newEmail)) {
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

            boolean isKeycloakUserUpdated = usersService.updateUser(existingEntity.getKeycloakUsername(),
                    dto.getUser());

            if (!isKeycloakUserUpdated)
                return false;
        }

        BossesModel updatedEntity = this.mapper.updateFromDto(dto, existingEntity);

        return updatedEntity != null; // Return true if the entity was updated successfully
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean deleteById(Integer id) {
        Optional<BossesModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Delete keycloak user
        boolean isKeycloakUserDeleted = usersService.deleteUserByUsername(optionalEntity.get().getKeycloakUsername());

        if (!isKeycloakUserDeleted)
            return false;

        // Delete the boss
        this.repository.deleteById(id);

        // Checking if the boss was deleted
        return !this.repository.findById(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        Optional<BossesModel> optionalEntity = this.repository.findByIdAndDeletedAtIsNotNull(id);

        if (!optionalEntity.isPresent())
            return true;

        // Restore the keycloak user
        boolean isKeycloakUserRestored = usersService.enabledUserByUsername(optionalEntity.get().getKeycloakUsername());

        if (!isKeycloakUserRestored)
            return false;

        // Restore the entity
        this.repository.restoreById(id);

        // Checking if the entity was restored
        return this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        Optional<BossesModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return true;

        // Disabled the user
        boolean isKeycloakUserDisabled = usersService
                .disabledUserByUsername(optionalEntity.get().getKeycloakUsername());

        if (!isKeycloakUserDisabled)
            return false;

        this.repository.softDeleteById(id, this.getUsername());

        // Checking if the entity was deleted
        return !this.repository.findByIdAndDeletedAtIsNull(id).isPresent();
    }

}
