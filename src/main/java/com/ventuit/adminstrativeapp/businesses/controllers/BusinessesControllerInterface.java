package com.ventuit.adminstrativeapp.businesses.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ventuit.adminstrativeapp.businesses.dto.BusinessesSearchCriteria;
import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.core.controllers.interfaces.CrudControllerInterface;

public interface BusinessesControllerInterface extends CrudControllerInterface<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, Integer> {

    @GetMapping("/search")
    ResponseEntity<Page<ListBusinessesDto>> searchNotDeleted(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable);

    @GetMapping("/search/all")
    ResponseEntity<Page<ListBusinessesDto>> searchAll(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable);

    @GetMapping("/search/deleted")
    ResponseEntity<Page<ListBusinessesDto>> searchDeleted(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable);
}
