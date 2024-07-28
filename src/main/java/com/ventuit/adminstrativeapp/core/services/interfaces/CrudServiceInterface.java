package com.ventuit.adminstrativeapp.core.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CrudServiceInterface<DTO, ID> {

    List<DTO> getAll();

    DTO getById(ID id);

    ResponseEntity<?> create(DTO dto);

    Boolean update(ID id, DTO dto);

    Boolean delete(ID id);

}
