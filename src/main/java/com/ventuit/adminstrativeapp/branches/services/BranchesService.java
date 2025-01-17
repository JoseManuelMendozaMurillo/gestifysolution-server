package com.ventuit.adminstrativeapp.branches.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.branches.dto.CreateBranchesDto;
import com.ventuit.adminstrativeapp.branches.dto.UpdateBranchesDto;
import com.ventuit.adminstrativeapp.branches.mappers.BranchesMapper;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.mappers.DirectionsMapper;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import com.ventuit.adminstrativeapp.shared.repositories.DirectionsRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class BranchesService
        extends
        CrudServiceImpl<CreateBranchesDto, UpdateBranchesDto, CreateBranchesDto, BranchesModel, Integer, BranchesMapper, BranchesRepository> {

    public BranchesService(BranchesRepository repository, BranchesMapper mapper) {
        super(repository, mapper);
    }

    @Autowired
    DirectionsRepository directionsRepository;
    @Autowired
    DirectionsMapper directionsMapper;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void create(CreateBranchesDto dto) {
        DirectionsModel direction = this.directionsMapper.toEntity(dto.getDirection());
        DirectionsModel directionSaved = this.directionsRepository.save(direction);
        DirectionsDto directionDto = this.directionsMapper.toDto(directionSaved);
        dto.setDirection(directionDto);
        super.create(dto);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean update(Integer id, UpdateBranchesDto dto) {
        Optional<BranchesModel> optionalBranch = this.repository.findById(id);

        if (!optionalBranch.isPresent())
            return false;

        BranchesModel branch = optionalBranch.get();

        if (dto.getDirection() != null) {

            DirectionsModel direction = branch.getDirection();

            dto.getDirection().setDeletedAt(direction.getDeletedAt());

            DirectionsModel directionUpdated = this.directionsMapper.updateFromDto(dto.getDirection(), direction);

            if (directionUpdated == null)
                return false;
        }

        dto.setDeletedAt(branch.getDeletedAt());

        BranchesModel branchUpdated = this.mapper.updateFromDto(dto, branch);

        return branchUpdated != null;
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        Optional<BranchesModel> optionalBranch = this.repository.findById(id);

        if (!optionalBranch.isPresent())
            return false;

        BranchesModel branch = optionalBranch.get();

        if (branch.getDirection() != null) {
            Integer directionId = branch.getDirection().getId();
            this.directionsRepository.softDeleteById(directionId);
        }

        return super.softDeleteById(id);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        Optional<BranchesModel> optionalBranch = this.repository.findById(id);

        if (!optionalBranch.isPresent())
            return false;

        BranchesModel branch = optionalBranch.get();

        if (branch.getDirection() != null) {
            Integer directionId = branch.getDirection().getId();
            this.directionsRepository.restoreById(directionId);
        }

        return super.restoreById(id);
    }

}
