package com.ventuit.adminstrativeapp.shared.repositories;

import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import com.ventuit.adminstrativeapp.shared.models.StorageProvidersModel;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageProvidersRepository extends BaseRepository<StorageProvidersModel, Integer> {

}