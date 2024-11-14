package com.ventuit.adminstrativeapp.core.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CrudServiceInterface<DTO, ID> {

    List<DTO> getAll();

    List<DTO> getAllInactive();

    List<DTO> getAllActive();

    DTO getById(ID id);

    DTO getByActiveId(ID id);

    DTO getByInactiveId(ID id);

    ResponseEntity<?> create(DTO dto);

    Boolean update(ID id, DTO dto);

    Boolean restoreById(ID id);

    Boolean softDeleteById(ID id);

    Boolean deleteById(ID id);
}
