package com.ventuit.adminstrativeapp.shared.repositories;

import com.ventuit.adminstrativeapp.shared.models.BucketsModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketsRepository extends BaseRepository<BucketsModel, Integer> {
}