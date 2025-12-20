package com.ventuit.adminstrativeapp.bosses.services.implementations;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.ListBossesDto;
import com.ventuit.adminstrativeapp.bosses.dto.UpdateBossesDto;
import com.ventuit.adminstrativeapp.bosses.mappers.BossesMapper;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.bosses.repositories.BossesRepository;
import com.ventuit.adminstrativeapp.bosses.services.interfaces.BossesServiceInterface;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.mappers.BusinessesMapper;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.keycloak.services.implementations.KeycloakUsersServiceImpl;
import com.ventuit.adminstrativeapp.keycloak.utils.KeycloakUtils;
import com.ventuit.adminstrativeapp.shared.exceptions.ObjectNotValidException;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class BossesServiceImpl
        extends
        CrudServiceImpl<CreateBossesDto, UpdateBossesDto, ListBossesDto, BossesModel, Integer, BossesMapper, BossesRepository>
        implements BossesServiceInterface {

    @Autowired
    private KeycloakUsersServiceImpl usersService;
    @Autowired
    private KeycloakUtils keycloakUtils;
    @Autowired
    private BusinessesRepository businessesRepository;
    @Autowired
    private BusinessesMapper businessesMapper;

    public BossesServiceImpl(BossesRepository repository, BossesMapper mapper) {
        super(repository, mapper);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListBossesDto create(CreateBossesDto dto) {
        // Validate the dto
        this.creatingDtoValidator.validate(dto);

        // Create keycloak user
        UserRepresentation user = usersService.createUser(dto.getUser());

        // Create the record in the database
        try {
            BossesModel entity = this.mapper.toEntity(dto);
            entity.setKeycloakUsername(user.getUsername());
            BossesModel entitySaved = this.repository.save(entity);
            entityManager.refresh(entitySaved);
            return this.mapper.toShowDto(entitySaved);
        } catch (Exception e) {
            usersService.deleteUserByUsername(user.getUsername());
            throw e;
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListBossesDto update(Integer id, UpdateBossesDto dto) {
        // Validate the dto
        this.updatingDtoValidator.validate(dto);

        Optional<BossesModel> optionalEntity = this.repository.findById(id);

        if (!optionalEntity.isPresent())
            return null;

        BossesModel existingEntity = optionalEntity.get();

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
                return null;
        }

        BossesModel updatedEntity = this.mapper.updateFromDto(dto, existingEntity);
        updatedEntity = this.repository.saveAndFlush(updatedEntity);
        return this.mapper.toShowDto(updatedEntity);
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

    @Override
    public Boolean isPhoneExists(String phone) {
        try {
            return this.repository.countByPhone(phone) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ListBusinessesDto> getBossesBusinesses(String bossIdOrUsername, Pageable pageable) {
        try {
            Integer bossId = null;
            boolean isId = NumberUtils.isCreatable(bossIdOrUsername);

            // 1. Resolve the Boss ID
            if (isId) {
                bossId = Integer.parseInt(bossIdOrUsername);
                // Optional: Check if boss exists if strict validation is needed
                if (!this.repository.existsById(bossId)) {
                    return null;
                }
            } else {
                // If username, look up the boss to get the ID
                BossesModel boss = this.repository.findByKeycloakUsername(bossIdOrUsername).orElse(null);
                if (boss == null) {
                    return null;
                }
                bossId = boss.getId();
            }

            // 2. Query the database using the Repository (Efficient Pagination)
            Page<BusinessesModel> businessesPage = this.businessesRepository.findBusinessesByBossId(bossId, pageable);

            // 3. Map the Page of Entities to a Page of DTOs
            return businessesPage.map(businessesMapper::toShowDto);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching boss's businesses: " + e.getMessage());
        }
    }

}
