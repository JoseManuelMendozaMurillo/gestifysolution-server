package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BusinessesTypeRepository extends JpaRepository<BusinessesTypeModel, Integer> {

}