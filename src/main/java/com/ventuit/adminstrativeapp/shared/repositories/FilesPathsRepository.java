package com.ventuit.adminstrativeapp.shared.repositories;

import com.ventuit.adminstrativeapp.shared.models.FilesPathsModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesPathsRepository extends BaseRepository<FilesPathsModel, Integer> {

    /**
     * Find path by its value
     */
    public FilesPathsModel findByPath(String path);

    /**
     * Check if path exists
     */
    public boolean existsByPath(String path);
}