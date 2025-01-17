package com.ventuit.adminstrativeapp.core.controllers.implementations;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ventuit.adminstrativeapp.core.controllers.interfaces.CrudControllerInterface;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public class CrudControllerImpl<CREATINGDTO, UPDATINGDTO, LISTDTO, ID, SERVICE extends CrudServiceInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID>>
        implements CrudControllerInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    protected SERVICE crudService;

    public CrudControllerImpl(SERVICE crudService) {
        this.crudService = crudService;
    }

    @Override
    public ResponseEntity<List<LISTDTO>> getAll() {
        List<LISTDTO> data = this.crudService.getAll();
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<List<LISTDTO>> getAllActive() {
        List<LISTDTO> data = this.crudService.getAllActive();
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<List<LISTDTO>> getAllInactive() {
        List<LISTDTO> data = this.crudService.getAllInactive();
        return ResponseEntity.ok(data);
    }

    @Override
    public ResponseEntity<?> getById(ID id) {
        LISTDTO record = this.crudService.getById(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> getByActiveId(ID id) {
        LISTDTO record = this.crudService.getByActiveId(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> getByInactiveId(ID id) {
        LISTDTO record = this.crudService.getByInactiveId(id);
        if (record == null)
            ResponseEntity.notFound().build();
        return ResponseEntity.ok(record);
    }

    @Override
    public ResponseEntity<?> create(CREATINGDTO model) {
        this.crudService.create(model);
        return ResponseEntity.ok("The record was created successfully");
    }

    @Override
    public ResponseEntity<String> update(ID id, UPDATINGDTO model) {
        Boolean isUpdatedEntity = this.crudService.update(id, model);
        if (isUpdatedEntity)
            return ResponseEntity.ok("Entity was updated");
        else
            return ResponseEntity.internalServerError().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> softDelete(ID id) {
        Boolean isDeletedEntity = this.crudService.softDeleteById(id);
        if (isDeletedEntity)
            return ResponseEntity.ok("Entity was soft deleted");
        else
            return ResponseEntity.internalServerError().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> delete(ID id) {
        Boolean isDeletedEntity = this.crudService.deleteById(id);
        if (isDeletedEntity)
            return ResponseEntity.ok("Entity was deleted");
        else
            return ResponseEntity.internalServerError().build();
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ResponseEntity<String> restore(ID id) {
        Boolean isRestoredEntity = this.crudService.restoreById(id);
        if (isRestoredEntity)
            return ResponseEntity.ok("Entity was restored");
        else
            return ResponseEntity.internalServerError().build();
    }
}
