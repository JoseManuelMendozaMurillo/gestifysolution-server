package com.ventuit.adminstrativeapp.core.controllers.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.core.controllers.interfaces.CrudControllerInterface;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public class CrudControllerImpl<CREATINGDTO, UPDATINGDTO, LISTDTO, ID, SERVICE extends CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID>>
        implements CrudControllerInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    protected SERVICE service;

    public CrudControllerImpl(SERVICE service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<LISTDTO>> getAll(Pageable pageable) {
        Page<LISTDTO> data = this.service.getAll(pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<Page<LISTDTO>> getAllActive(Pageable pageable) {
        Page<LISTDTO> data = this.service.getAllActive(pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<Page<LISTDTO>> getAllInactive(Pageable pageable) {
        Page<LISTDTO> data = this.service.getAllInactive(pageable);
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<?> getById(ID id) {
        LISTDTO record = this.service.getById(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> getByActiveId(ID id) {
        LISTDTO record = this.service.getByActiveId(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> getByInactiveId(ID id) {
        LISTDTO record = this.service.getByInactiveId(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> create(CREATINGDTO model) {
        LISTDTO entityCreated = this.service.create(model);
        return ResponseEntity.ok(entityCreated);
    }

    @Override
    public ResponseEntity<?> update(ID id, UPDATINGDTO model) {
        LISTDTO entityUpdated = this.service.update(id, model);
        if (entityUpdated != null)
            return ResponseEntity.ok(entityUpdated);
        else
            return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> softDelete(ID id) {
        Boolean isDeletedEntity = this.service.softDeleteById(id);
        if (isDeletedEntity)
            return ResponseEntity.ok("Entity was soft deleted");
        else
            return ResponseEntity.internalServerError().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> delete(ID id) {
        Boolean isDeletedEntity = this.service.deleteById(id);
        if (isDeletedEntity)
            return ResponseEntity.ok("Entity was deleted");
        else
            return ResponseEntity.internalServerError().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> restore(ID id) {
        Boolean isRestoredEntity = this.service.restoreById(id);
        if (isRestoredEntity)
            return ResponseEntity.ok("Entity was restored");
        else
            return ResponseEntity.internalServerError().build();
    }
}
