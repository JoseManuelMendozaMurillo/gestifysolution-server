package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;

@Repository
public interface BusinessesRepository extends JpaRepository<BusinessesModel, Integer> {

}
