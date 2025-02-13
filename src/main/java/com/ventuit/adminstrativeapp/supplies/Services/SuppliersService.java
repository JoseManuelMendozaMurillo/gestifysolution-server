package com.ventuit.adminstrativeapp.supplies.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.mappers.DirectionsMapper;
import com.ventuit.adminstrativeapp.shared.models.DirectionsModel;
import com.ventuit.adminstrativeapp.shared.repositories.DirectionsRepository;
import com.ventuit.adminstrativeapp.supplies.dto.CreateSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.dto.ListSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.dto.UpdateSuppliersDto;
import com.ventuit.adminstrativeapp.supplies.mappers.SuppliersMapper;
import com.ventuit.adminstrativeapp.supplies.models.SuppliersModel;
import com.ventuit.adminstrativeapp.supplies.repositories.SuppliersRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class SuppliersService extends
        CrudServiceImpl<CreateSuppliersDto, UpdateSuppliersDto, ListSuppliersDto, SuppliersModel, Integer, SuppliersMapper, SuppliersRepository> {

    public SuppliersService(SuppliersMapper mapper, SuppliersRepository repository) {
        super(repository, mapper);
    }

    @Autowired
    DirectionsRepository directionsRepository;
    @Autowired
    DirectionsMapper directionsMapper;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void create(CreateSuppliersDto dto) {
        if (dto.getDirection() != null) {
            dto.getDirection().setCreatedBy(getUsername());
            DirectionsModel direction = this.directionsMapper.toEntity(dto.getDirection());
            DirectionsModel directionSaved = this.directionsRepository.save(direction);
            DirectionsDto directionDto = this.directionsMapper.toDto(directionSaved);
            dto.setDirection(directionDto);
        }
        super.create(dto);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean update(Integer id, UpdateSuppliersDto dto) {
        Optional<SuppliersModel> optionalSupplier = this.repository.findById(id);

        if (!optionalSupplier.isPresent())
            return false;

        SuppliersModel supplier = optionalSupplier.get();

        if (dto.getDirection() != null) {
            DirectionsModel directionToUpdate = supplier.getDirection();

            if (directionToUpdate != null) {
                // In case to update the direction
                dto.getDirection().setUpdatedBy(getUsername());
                DirectionsModel directionUpdated = this.directionsMapper.updateFromDto(dto.getDirection(),
                        directionToUpdate);
                if (directionUpdated == null)
                    return false;
            } else {
                // In case to create a new direction
                dto.getDirection().setCreatedBy(getUsername());
                DirectionsModel newDirection = this.directionsMapper.toEntity(dto.getDirection());
                DirectionsModel newDirectionSaved = this.directionsRepository.save(newDirection);
                supplier.setDirection(newDirectionSaved);
            }

        }

        dto.setDeletedAt(supplier.getDeletedAt());

        SuppliersModel branchUpdated = this.mapper.updateFromDto(dto, supplier);

        return branchUpdated != null;
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean softDeleteById(Integer id) {
        Optional<SuppliersModel> optionalSupplier = this.repository.findById(id);

        if (!optionalSupplier.isPresent())
            return false;

        SuppliersModel supplier = optionalSupplier.get();

        if (supplier.getDirection() != null) {
            Integer directionId = supplier.getDirection().getId();
            this.directionsRepository.softDeleteById(directionId, getUsername());
        }

        return super.softDeleteById(id);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public Boolean restoreById(Integer id) {
        Optional<SuppliersModel> optionalSupplier = this.repository.findById(id);

        if (!optionalSupplier.isPresent())
            return false;

        SuppliersModel supplier = optionalSupplier.get();

        if (supplier.getDirection() != null) {
            Integer directionId = supplier.getDirection().getId();
            this.directionsRepository.restoreById(directionId);
        }

        return super.restoreById(id);
    }
}
