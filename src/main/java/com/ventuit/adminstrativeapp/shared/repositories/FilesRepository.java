package com.ventuit.adminstrativeapp.shared.repositories;

import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends BaseRepository<FilesModel, Integer> {

    /**
     * Find file by its storage key
     */
    public FilesModel findByFileKey(String fileKey);

    /**
     * Check if file exists by key
     */
    public boolean existsByFileKey(String fileKey);
}