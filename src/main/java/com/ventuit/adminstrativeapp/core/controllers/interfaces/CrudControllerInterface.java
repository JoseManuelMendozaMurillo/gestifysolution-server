package com.ventuit.adminstrativeapp.core.controllers.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface CrudControllerInterface<CREATINGDTO, UPDATINGDTO, LISTDTO, ID> {

    @GetMapping()
    @ResponseBody
    ResponseEntity<List<LISTDTO>> getAll();

    @GetMapping("/get-all-active")
    @ResponseBody
    ResponseEntity<List<LISTDTO>> getAllActive();

    @GetMapping("/get-all-inactive")
    @ResponseBody
    ResponseEntity<List<LISTDTO>> getAllInactive();

    @GetMapping("/{id}")
    @ResponseBody
    ResponseEntity<?> getById(@PathVariable ID id);

    @GetMapping("/get-by-active-id/{id}")
    @ResponseBody
    ResponseEntity<?> getByActiveId(@PathVariable ID id);

    @GetMapping("/get-by-inactive-id/{id}")
    @ResponseBody
    ResponseEntity<?> getByInactiveId(@PathVariable ID id);

    @PostMapping()
    @ResponseBody
    ResponseEntity<?> create(@RequestBody CREATINGDTO dto);

    @PutMapping("/{id}")
    @ResponseBody
    ResponseEntity<?> update(@PathVariable ID id, @RequestBody UPDATINGDTO dto);

    @DeleteMapping("soft/{id}")
    @ResponseBody
    ResponseEntity<String> softDelete(@PathVariable ID id);

    @DeleteMapping("/{id}")
    @ResponseBody
    ResponseEntity<String> delete(@PathVariable ID id);

    @PatchMapping("restore/{id}")
    @ResponseBody
    ResponseEntity<String> restore(@PathVariable ID id);
}