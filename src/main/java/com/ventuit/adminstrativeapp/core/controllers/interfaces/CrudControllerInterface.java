package com.ventuit.adminstrativeapp.core.controllers.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface CrudControllerInterface<DTO, ID> {

    @GetMapping
    @ResponseBody
    ResponseEntity<List<DTO>> getAll();

    @GetMapping("/getById/{id}")
    @ResponseBody
    ResponseEntity<?> getById(@PathVariable ID id);

    @PostMapping()
    @ResponseBody
    ResponseEntity<?> create(@RequestBody DTO dto);

    @PutMapping()
    @ResponseBody
    ResponseEntity<String> update(@PathVariable ID id, @RequestBody DTO dto);

    @DeleteMapping()
    @ResponseBody
    ResponseEntity<String> delete(@PathVariable ID id);
}