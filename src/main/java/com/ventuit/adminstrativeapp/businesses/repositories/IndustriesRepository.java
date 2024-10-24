package com.ventuit.adminstrativeapp.businesses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;

@Repository
public interface IndustriesRepository extends JpaRepository<IndustriesModel, Integer> {

}
