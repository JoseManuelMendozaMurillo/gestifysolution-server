package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface IndustriesRepository extends BaseRepository<IndustriesModel, Integer> {

}
