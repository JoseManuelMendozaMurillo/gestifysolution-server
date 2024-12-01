package com.ventuit.adminstrativeapp.branches.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

@Repository
public interface BranchesRepository extends BaseRepository<BranchesModel, Integer> {

}