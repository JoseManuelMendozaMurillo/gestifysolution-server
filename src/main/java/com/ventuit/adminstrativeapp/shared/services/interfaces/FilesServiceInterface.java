package com.ventuit.adminstrativeapp.shared.services.interfaces;

import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;

public interface FilesServiceInterface {

    public FilesModel uploadFile(FileUploadDto uploadDto);

    public FileResponseDto getFile(Integer fileId);

    public FileResponseDto getFileByKey(String key);

    public FilesModel updateFile(Integer fileId, FileUploadDto uploadDto);

    public boolean softDeleteFileFromBucket(Integer fileId, Integer bucketId, String deletedBy);

    public boolean softDeleteFileFromAllBuckets(Integer fileId, String deletedBy);

    public boolean restoreFileFromAllBuckets(Integer fileId);

    public boolean deleteFileFromAllBuckets(Integer fileId);

    public void deleteAllFiles();

}
