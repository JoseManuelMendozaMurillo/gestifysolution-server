package com.ventuit.adminstrativeapp.businesses.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ventuit.adminstrativeapp.businesses.dto.BusinessesSearchCriteria;
import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.UpdateBusinessesDto;
import com.ventuit.adminstrativeapp.core.services.interfaces.CrudServiceInterface;

public interface BusinessesServiceInterface
        extends CrudServiceInterface<CreateBusinessesDto, UpdateBusinessesDto, ListBusinessesDto, Integer> {

    Page<ListBusinessesDto> searchBusinesses(BusinessesSearchCriteria criteria, Pageable pageable);
}
