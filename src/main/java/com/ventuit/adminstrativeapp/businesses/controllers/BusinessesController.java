package com.ventuit.adminstrativeapp.businesses.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.services.BusinessesService;
import com.ventuit.adminstrativeapp.core.controllers.implementations.CrudControllerImpl;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.businesses.dto.BusinessesSearchCriteria;
import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

@RestController
@RequestMapping("businesses")
public class BusinessesController
        extends
        CrudControllerImpl<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, Integer, BusinessesService>
        implements BusinessesControllerInterface {

    public BusinessesController(BusinessesService service) {
        super(service);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListBusinessesDto> create(@ModelAttribute CreateBusinessesDto createBusinessesDto) {
        ListBusinessesDto entityCreated = this.service.create(createBusinessesDto);
        return ResponseEntity.ok(entityCreated);
    }

    @Override
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListBusinessesDto> update(@PathVariable Integer id,
            @ModelAttribute UpdateBusinessesDto businessesDto) {
        ListBusinessesDto entityCreated = this.service.update(id, businessesDto);
        return ResponseEntity.ok(entityCreated);
    }

    @GetMapping("/{businessId}/products")
    public ResponseEntity<Page<ListProductDto>> getProductsByBusinessId(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(this.service.getProductsByBusinessId(businessId, categoryId, pageable));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<Page<ListBusinessesDto>> searchNotDeleted(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.NOT_DELETED);
        return ResponseEntity.ok(this.service.searchBusinesses(criteria, pageable));
    }

    @Override
    @GetMapping("/search/all")
    public ResponseEntity<Page<ListBusinessesDto>> searchAll(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.ALL);
        return ResponseEntity.ok(this.service.searchBusinesses(criteria, pageable));
    }

    @Override
    @GetMapping("/search/deleted")
    public ResponseEntity<Page<ListBusinessesDto>> searchDeleted(@ModelAttribute BusinessesSearchCriteria criteria, Pageable pageable) {
        criteria.setDeletionStatus(DeletionStatus.DELETED);
        return ResponseEntity.ok(this.service.searchBusinesses(criteria, pageable));
    }

}
