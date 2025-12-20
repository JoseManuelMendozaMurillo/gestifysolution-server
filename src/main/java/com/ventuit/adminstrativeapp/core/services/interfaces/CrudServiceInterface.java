package com.ventuit.adminstrativeapp.core.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    Page<LISTDTO> getAll(Pageable pageable);

    Page<LISTDTO> getAllInactive(Pageable pageable);

    Page<LISTDTO> getAllActive(Pageable pageable);

    LISTDTO getById(ID id);

    LISTDTO getByActiveId(ID id);

    LISTDTO getByInactiveId(ID id);

    LISTDTO create(CREATINGDTO dto);

    LISTDTO update(ID id, UPDATINGDTO dto);

    Boolean restoreById(ID id);

    Boolean softDeleteById(ID id);

    Boolean deleteById(ID id);
}
