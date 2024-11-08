package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface BusinessesTypeRepository extends BaseRepository<BusinessesTypeModel, Integer> {

}