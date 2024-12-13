package com.ventuit.adminstrativeapp.core.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    List<LISTDTO> getAll();

    List<LISTDTO> getAllInactive();

    List<LISTDTO> getAllActive();

    LISTDTO getById(ID id);

    LISTDTO getByActiveId(ID id);

    LISTDTO getByInactiveId(ID id);

    ResponseEntity<?> create(CREATINGDTO dto);

    Boolean update(ID id, UPDATINGDTO dto);

    Boolean restoreById(ID id);

    Boolean softDeleteById(ID id);

    Boolean deleteById(ID id);
}
