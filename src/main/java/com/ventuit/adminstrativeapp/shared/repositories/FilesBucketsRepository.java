package com.ventuit.adminstrativeapp.shared.repositories;

import com.ventuit.adminstrativeapp.shared.models.FilesBucketsModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface FilesBucketsRepository extends BaseRepository<FilesBucketsModel, Integer> {

    /**
     * Find file-bucket associations by file ID
     */
    public List<FilesBucketsModel> findByFileId(Integer fileId);

    /**
     * Find file-bucket associations by bucket ID
     */
    public List<FilesBucketsModel> findByBucketId(Integer bucketId);

    /**
     * Find file-bucket association by file and bucket
     */
    public FilesBucketsModel findByFileIdAndBucketId(Integer fileId, Integer bucketId);
}