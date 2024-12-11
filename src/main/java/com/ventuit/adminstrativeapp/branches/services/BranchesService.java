package com.ventuit.adminstrativeapp.branches.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.branches.dto.BranchesDto;
import com.ventuit.adminstrativeapp.branches.mappers.BranchesMapper;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.mappers.DirectionsMapper;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import com.ventuit.adminstrativeapp.shared.repositories.DirectionsRepository;

@Service
public class BranchesService
        extends CrudServiceImpl<BranchesDto, BranchesModel, Integer, BranchesMapper, BranchesRepository> {

    public BranchesService(BranchesRepository repository, BranchesMapper mapper) {
        super(repository, mapper);
    }

    @Autowired
    DirectionsRepository directionsRepository;
    @Autowired
    DirectionsMapper directionsMapper;

    @Override
    public ResponseEntity<?> create(BranchesDto dto) {
        DirectionsModel direction = this.directionsMapper.toEntity(dto.getDirection());
        DirectionsModel directionSaved = this.directionsRepository.save(direction);
        DirectionsDto directionDto = this.directionsMapper.toDto(directionSaved);
        dto.setDirection(directionDto);
        return super.create(dto);
    }

    @Override
    public Boolean update(Integer id, BranchesDto dto) {
        Optional<BranchesModel> optionalBranch = this.repository.findById(id);

        if (!optionalBranch.isPresent())
            return false;

        BranchesModel branch = optionalBranch.get();

        DirectionsModel direction = branch.getDirection();

        dto.getDirection().setDeletedAt(direction.getDeletedAt());

        DirectionsModel directionUpdated = this.directionsMapper.updateFromDto(dto.getDirection(), direction);

        if (directionUpdated == null)
            return false;

        dto.setDeletedAt(branch.getDeletedAt());

        BranchesModel branchUpdated = this.mapper.updateFromDto(dto, branch);

        return branchUpdated != null;
    }

    @Override
    public Boolean softDeleteById(Integer id) {
        Optional<DirectionsModel> optionalDirection = this.directionsRepository.findById(id);

        if (optionalDirection.isPresent()) {
            // Deleting the entity
            Integer directionId = optionalDirection.get().getId();
            this.directionsRepository.softDeleteById(directionId);
        }

        return super.softDeleteById(id);
    }

    @Override
    public Boolean restoreById(Integer id) {
        Optional<DirectionsModel> optionalDirection = this.directionsRepository.findById(id);

        if (optionalDirection.isPresent()) {
            // Deleting the entity
            Integer directionId = optionalDirection.get().getId();
            this.directionsRepository.restoreById(directionId);
        }

        return super.restoreById(id);
    }

}
