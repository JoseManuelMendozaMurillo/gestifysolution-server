package com.ventuit.adminstrativeapp.shared.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ventuit.adminstrativeapp.shared.dto.FileResponseDto;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.models.BucketsModel;
import com.ventuit.adminstrativeapp.shared.models.FilesBucketsModel;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.models.FilesPathsModel;
import com.ventuit.adminstrativeapp.shared.repositories.BucketsRepository;
import com.ventuit.adminstrativeapp.shared.repositories.FilesBucketsRepository;
import com.ventuit.adminstrativeapp.shared.repositories.FilesPathsRepository;
import com.ventuit.adminstrativeapp.shared.repositories.FilesRepository;
import com.ventuit.adminstrativeapp.storage.minio.services.MinioService;

import com.ventuit.adminstrativeapp.core.services.BlurHashService;
import com.ventuit.adminstrativeapp.shared.exceptions.FileDeleteException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileGetException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

/**
 * Service for managing file uploads, storage, and database records.
 * Integrates with MinIO for file storage and maintains file metadata in the
 * database.nextOrder
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FilesService {

    private static final Logger log = LoggerFactory.getLogger(FilesService.class);
    private final MinioService minioService;
    private final FilesRepository filesRepository;
    private final FilesPathsRepository filesPathsRepository;
    private final FilesBucketsRepository filesBucketsRepository;
    private final BucketsRepository bucketsRepository;
    private final BlurHashService blurHashService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${app.domain}")
    private String domain;

    @Value("${server.port}")
    private String serverPort;

    /**
     * Upload a file to storage and register it in the database
     * 
     * @param uploadDto File upload data
     * @return File model
     */
    public FilesModel uploadFile(FileUploadDto uploadDto) {
        String fileKey = null;
        String bucketName = null;
        try {
            // Generate unique file key (without path)
            fileKey = generateFileKey(uploadDto.getOriginalFileName());

            // Get file extension
            String extension = getFileExtension(uploadDto.getOriginalFileName());

            // Get or create file path record
            FilesPathsModel filePath = getOrCreateFilePath(uploadDto.getPath());

            // Get bucket from database
            BucketsModel bucket = getBucketFromStorage(uploadDto.getBucket(), uploadDto.getProvider());
            bucketName = bucket.getName();

            // Compose the object name for MinIO (path + fileKey)
            String objectName;
            if (uploadDto.getPath() != null && !uploadDto.getPath().trim().isEmpty()) {
                String normalizedPath = uploadDto.getPath().replaceAll("^/+", "").replaceAll("/+$", "");
                objectName = normalizedPath + "/" + fileKey;
            } else {
                objectName = fileKey;
            }

            // Upload file to MinIO using MinioService
            uploadToMinIO(uploadDto, objectName, bucketName);

            // Always generate blurhash if image
            String blurHash = null;
            if (uploadDto.getContentType() != null && uploadDto.getContentType().startsWith("image/")) {
                uploadDto.getFileInputStream().reset();
                blurHash = blurHashService.generateBlurHash(uploadDto.getFileInputStream(), 4, 3);
            }

            // Create file record in database (save only fileKey, not path)
            FilesModel fileRecord = createFileRecord(uploadDto, fileKey, extension, filePath, blurHash);

            // Create file-bucket association
            createFileBucketAssociation(fileRecord, bucket);

            // Return the file record
            return fileRecord;

        } catch (Exception e) {
            // If fileKey and bucketName are set, try to delete the file from MinIO
            if (fileKey != null && bucketName != null) {
                try {
                    minioService.deleteFile(fileKey);
                    log.warn("Rolled back uploaded file from MinIO: {} in bucket: {}", fileKey, bucketName);
                } catch (Exception ex) {
                    log.error("Failed to rollback uploaded file from MinIO: {} in bucket: {}", fileKey, bucketName, ex);
                }
            }
            log.error("Error uploading file: {}", uploadDto.getOriginalFileName(), e);
            throw new FileUploadException("Failed to upload file: " + uploadDto.getOriginalFileName(), e);
        }
    }

    /**
     * Force delete a file from the database using a native query
     */
    public void forceDeleteFileById(Integer fileId) {
        entityManager.createNativeQuery("DELETE FROM files WHERE id = :id")
                .setParameter("id", fileId)
                .executeUpdate();
    }

    /**
     * Delete a file from storage and database
     * 
     * @param fileId File ID to delete
     * @return true if deleted successfully
     */
    @Transactional
    public boolean deleteFileFromAllBuckets(Integer fileId) {
        try {
            // 1. Get the file record
            FilesModel file = filesRepository.findById(fileId).orElse(null);
            if (file == null) {
                log.warn("File not found with ID: {}", fileId);
                return false;
            }

            // Gather info for MinIO deletion BEFORE deleting the file
            FilesPathsModel filePath = file.getFilesPaths();
            String completeFilePath = (filePath != null) ? filePath.getPath() + "/" + file.getFileKey() : null;
            String bucketName = null;
            try {
                bucketName = getBucketForFile(fileId).getName();
            } catch (Exception e) {
                log.warn("Could not get bucket for file before deletion: {}", e.getMessage());
            }

            // Remove file from FilesPathsModel.files (bidirectional)
            if (filePath != null && filePath.getFiles() != null) {
                filePath.getFiles().remove(file);
            }
            file.setFilesPaths(null);

            // Remove all FilesBucketsModel associations
            filesBucketsRepository.findByFileId(fileId).forEach(fb -> filesBucketsRepository.deleteById(fb.getId()));

            // Delete the file
            filesRepository.delete(file);

            // If the filePath exists and now has no files, delete it
            if (filePath != null && (filePath.getFiles() == null || filePath.getFiles().isEmpty())) {
                filesPathsRepository.deleteById(filePath.getId());
                log.info("Deleted file path with id {} as it has no more files", filePath.getId());
            }

            // Delete from MinIO using MinioService (AFTER DB delete)
            if (completeFilePath != null && bucketName != null) {
                deleteFromMinIO(completeFilePath, bucketName);
            }

            log.info("Successfully deleted file: {} (ID: {})", file.getName(), fileId);
            return true;
        } catch (Exception e) {
            log.error("Error deleting file with ID: {}", fileId, e);
            throw new FileDeleteException("Failed to delete file with ID: " + fileId, e);
        }
    }

    /**
     * Get file by ID
     * 
     * @param fileId File ID
     * @return File response or null if not found
     */
    public FileResponseDto getFile(Integer fileId) {
        try {
            FilesModel file = filesRepository.findById(fileId)
                    .orElse(null);

            if (file == null) {
                return null;
            }

            FilesPathsModel filePath = file.getFilesPaths();
            BucketsModel bucket = getBucketForFile(fileId);

            return buildFileResponse(file, filePath, bucket);

        } catch (Exception e) {
            log.error("Error getting file with ID: {}", fileId, e);
            throw new FileGetException("Failed to get file with ID: " + fileId, e);
        }
    }

    /**
     * Get file by storage key
     * 
     * @param key File storage key
     * @return File response or null if not found
     */
    public FileResponseDto getFileByKey(String key) {
        try {
            FilesModel file = filesRepository.findByFileKey(key);

            if (file == null) {
                return null;
            }

            FilesPathsModel filePath = file.getFilesPaths();
            BucketsModel bucket = getBucketForFile(file.getId());

            return buildFileResponse(file, filePath, bucket);

        } catch (Exception e) {
            log.error("Error getting file with key: {}", key, e);
            throw new FileGetException("Failed to get file with key: " + key, e);
        }
    }

    /**
     * Soft delete a file from a specific bucket, and cascade as needed.
     *
     * @param fileId    the file ID
     * @param bucketId  the bucket ID
     * @param deletedBy the username or system performing the delete
     * @return true if the operation was successful
     */
    @Transactional
    public boolean softDeleteFileFromBucket(Integer fileId, Integer bucketId, String deletedBy) {
        // 1. Soft delete the files_buckets association for this file and bucket
        FilesBucketsModel fileBucket = filesBucketsRepository.findByFileIdAndBucketId(fileId, bucketId);
        if (fileBucket == null) {
            log.warn("No file-bucket association found for fileId {} and bucketId {}", fileId, bucketId);
            return false;
        }
        filesBucketsRepository.softDelete(fileBucket, deletedBy);
        log.info("Soft deleted files_buckets association for fileId {} and bucketId {}", fileId, bucketId);

        // 2. Check if there are any other active (not soft deleted) associations for
        // this file
        FilesModel file = fileBucket.getFile();
        List<FilesBucketsModel> activeFileBuckets = file.getFilesBuckets().stream()
                .filter(fb -> fb.getDeletedAt() == null)
                .toList();
        if (activeFileBuckets.isEmpty()) {
            // 3. Soft delete the file itself
            filesRepository.softDelete(file, deletedBy);
            log.info("Soft deleted file with id {} as it has no more active bucket associations", fileId);

            // 4. If the file has a path, check if any other active files reference it
            FilesPathsModel filePath = file.getFilesPaths();
            if (filePath != null) {
                List<FilesModel> activeFilesInPath = filePath.getFiles().stream()
                        .filter(f -> f.getDeletedAt() == null)
                        .toList();
                if (activeFilesInPath.isEmpty()) {
                    filesPathsRepository.softDelete(filePath, deletedBy);
                    log.info("Soft deleted file path with id {} as it has no more active files", filePath.getId());
                }
            }
        }
        // Never delete from MinIO in this method
        return true;
    }

    /**
     * Soft delete a file from all buckets it's associated with
     * 
     * @param fileId    File ID to delete from all buckets
     * @param deletedBy User who is performing the deletion
     * @return true if deleted successfully from all buckets
     */
    @Transactional
    public boolean softDeleteFileFromAllBuckets(Integer fileId, String deletedBy) {
        try {
            // 1. Get the file record
            FilesModel file = filesRepository.findById(fileId).orElse(null);
            if (file == null) {
                log.warn("File not found with ID: {}", fileId);
                return false;
            }

            // 2. Get all active file-bucket associations for this file
            List<FilesBucketsModel> activeFileBuckets = file.getFilesBuckets().stream()
                    .filter(fb -> fb.getDeletedAt() == null)
                    .toList();

            if (activeFileBuckets.isEmpty()) {
                log.warn("No active file-bucket associations found for fileId {}", fileId);
                return false;
            }

            // 3. Soft delete all file-bucket associations
            for (FilesBucketsModel fileBucket : activeFileBuckets) {
                filesBucketsRepository.softDelete(fileBucket, deletedBy);
                log.info("Soft deleted files_buckets association for fileId {} and bucketId {}",
                        fileId, fileBucket.getBucket().getId());
            }

            // 4. Soft delete the file itself since it has no more active bucket
            // associations
            filesRepository.softDelete(file, deletedBy);
            log.info("Soft deleted file with id {} as it has no more active bucket associations", fileId);

            // 5. If the file has a path, check if any other active files reference it
            FilesPathsModel filePath = file.getFilesPaths();
            if (filePath != null) {
                List<FilesModel> activeFilesInPath = filePath.getFiles().stream()
                        .filter(f -> f.getDeletedAt() == null)
                        .toList();
                if (activeFilesInPath.isEmpty()) {
                    filesPathsRepository.softDelete(filePath, deletedBy);
                    log.info("Soft deleted file path with id {} as it has no more active files", filePath.getId());
                }
            }

            log.info("Successfully soft deleted file {} from all {} buckets", fileId, activeFileBuckets.size());
            return true;

        } catch (Exception e) {
            log.error("Error soft deleting file with ID {} from all buckets", fileId, e);
            throw new FileDeleteException("Failed to soft delete file with ID " + fileId + " from all buckets", e);
        }
    }

    /**
     * Restore a file from all buckets it was previously soft deleted from
     * 
     * @param fileId File ID to restore from all buckets
     * @return true if restored successfully from all buckets
     */
    @Transactional
    public boolean restoreFileFromAllBuckets(Integer fileId) {
        try {
            // 1. Get the file record (including soft deleted ones)
            FilesModel file = filesRepository.findByIdAndDeletedAtIsNotNull(fileId).orElse(null);
            if (file == null) {
                log.warn("Soft deleted file not found with ID: {}", fileId);
                return false;
            }

            // 2. Get all soft deleted file-bucket associations for this file
            List<FilesBucketsModel> softDeletedFileBuckets = file.getFilesBuckets().stream()
                    .filter(fb -> fb.getDeletedAt() != null)
                    .toList();

            if (softDeletedFileBuckets.isEmpty()) {
                log.warn("No soft deleted file-bucket associations found for fileId {}", fileId);
                return false;
            }

            // 3. Restore all file-bucket associations
            for (FilesBucketsModel fileBucket : softDeletedFileBuckets) {
                filesBucketsRepository.restore(fileBucket);
                log.info("Restored files_buckets association for fileId {} and bucketId {}",
                        fileId, fileBucket.getBucket().getId());
            }

            // 4. Restore the file itself
            filesRepository.restore(file);
            log.info("Restored file with id {}", fileId);

            // 5. If the file has a path that was soft deleted, restore it too
            FilesPathsModel filePath = file.getFilesPaths();
            if (filePath != null && filePath.getDeletedAt() != null) {
                filesPathsRepository.restore(filePath);
                log.info("Restored file path with id {} as it has no more active files", filePath.getId());
            }

            log.info("Successfully restored file {} from all {} buckets", fileId, softDeletedFileBuckets.size());
            return true;

        } catch (Exception e) {
            log.error("Error restoring file with ID {} from all buckets", fileId, e);
            return false;
        }
    }

    /**
     * Update/replace an existing file with a new one
     * 
     * @param fileId    File ID to update
     * @param uploadDto New file upload data
     * @return Updated file model
     */
    @Transactional
    public FilesModel updateFile(Integer fileId, FileUploadDto uploadDto) {
        String oldFileKey = null;
        String newFileKey = null;
        String bucketName = null;

        try {
            // 1. Get the existing file record
            FilesModel existingFile = filesRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

            oldFileKey = existingFile.getFileKey();

            // 2. Get bucket information
            BucketsModel bucket = getBucketForFile(fileId);
            bucketName = bucket.getName();

            // 3. Generate new file key for the replacement file
            newFileKey = generateFileKey(uploadDto.getOriginalFileName());

            // 4. Get file extension
            String extension = getFileExtension(uploadDto.getOriginalFileName());

            // 5. Get or create file path record (use existing path if available)
            FilesPathsModel filePath = existingFile.getFilesPaths();
            oldFileKey = filePath.getPath() + "/" + existingFile.getFileKey();
            if (uploadDto.getPath() != null && !uploadDto.getPath().trim().isEmpty()) {
                filePath = getOrCreateFilePath(uploadDto.getPath());
            }

            // 6. Compose the object name for MinIO (path + fileKey)
            String objectName;
            if (uploadDto.getPath() != null && !uploadDto.getPath().trim().isEmpty()) {
                String normalizedPath = uploadDto.getPath().replaceAll("^/+", "").replaceAll("/+$", "");
                objectName = normalizedPath + "/" + newFileKey;
            } else {
                objectName = newFileKey;
            }

            // 7. Upload new file to MinIO
            uploadToMinIO(uploadDto, objectName, bucketName);

            // 8. Generate blurhash if image
            String blurHash = null;
            if (uploadDto.getContentType() != null && uploadDto.getContentType().startsWith("image/")) {
                uploadDto.getFileInputStream().reset();
                blurHash = blurHashService.generateBlurHash(uploadDto.getFileInputStream(), 4, 3);
            }

            // 9. Update the file record in database
            existingFile.setFileKey(newFileKey);
            existingFile.setName(getFileName(uploadDto.getOriginalFileName()));
            existingFile.setExtension(extension.substring(1));
            existingFile.setSize(uploadDto.getFileSize().doubleValue());
            existingFile.setBlurHash(blurHash);
            existingFile.setFilesPaths(filePath);

            FilesModel updatedFile = filesRepository.save(existingFile);

            // 10. Delete the old file from MinIO
            try {
                deleteFromMinIO(oldFileKey, bucketName);
                log.info("Successfully deleted old file from MinIO: {} in bucket: {}", oldFileKey, bucketName);
            } catch (Exception e) {
                log.warn("Failed to delete old file from MinIO: {} in bucket: {}", oldFileKey, bucketName, e);
                // Don't throw exception here as the update was successful
            }

            log.info("Successfully updated file: {} (ID: {})", uploadDto.getOriginalFileName(), fileId);
            return updatedFile;

        } catch (Exception e) {
            // If newFileKey and bucketName are set, try to delete the new file from MinIO
            if (newFileKey != null && bucketName != null) {
                try {
                    minioService.deleteFile(newFileKey);
                    log.warn("Rolled back uploaded new file from MinIO: {} in bucket: {}", newFileKey, bucketName);
                } catch (Exception ex) {
                    log.error("Failed to rollback uploaded new file from MinIO: {} in bucket: {}", newFileKey,
                            bucketName, ex);
                }
            }
            log.error("Error updating file with ID: {}", fileId, e);
            throw new FileUploadException("Failed to update file with ID: " + fileId, e);
        }
    }

    // Private helper methods

    private String generateFileKey(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = getFileExtension(originalFileName);
        String fileName = getFileName(originalFileName);
        return fileName + "_" + timestamp + "_" + uuid + extension;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getFileName(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName == null ? null : fileName.replace(" ", "-");
        }
        // Return the name without the extension and replace spaces with dashes
        return fileName.substring(0, fileName.lastIndexOf(".")).replace(" ", "-");
    }

    private FilesPathsModel getOrCreateFilePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return null;
        }

        // Normalize path
        String normalizedPath = path.startsWith("/") ? path : "/" + path;

        // Check if path exists
        FilesPathsModel existingPath = filesPathsRepository.findByPath(normalizedPath);
        if (existingPath != null) {
            filesPathsRepository.restore(existingPath);
            return existingPath;
        }

        // Create new path
        FilesPathsModel newPath = FilesPathsModel.builder()
                .path(normalizedPath)
                .build();

        return filesPathsRepository.save(newPath);
    }

    private BucketsModel getBucketFromStorage(String bucketName, String provider) {
        return bucketsRepository.findAll().stream()
                .filter(bucket -> bucket.getName().equals(bucketName)
                        && bucket.getStorageProvider().getProvider().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Bucket not found: " + bucketName + " with provider: " + provider));
    }

    private void uploadToMinIO(FileUploadDto uploadDto, String objectName, String bucketName) {
        try {
            // Use MinioService to upload the file with InputStream
            minioService.uploadFile(objectName, uploadDto.getFileInputStream(), uploadDto.getFileSize(),
                    uploadDto.getContentType());
            log.info("Successfully uploaded file to MinIO: {} in bucket: {}", objectName, bucketName);
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    private FilesModel createFileRecord(FileUploadDto uploadDto, String fileKey, String extension,
            FilesPathsModel filePath, String blurHash) {
        FilesModel fileRecord = FilesModel.builder()
                .fileKey(fileKey)
                .name(getFileName(uploadDto.getOriginalFileName()))
                .extension(extension.substring(1))
                .size(uploadDto.getFileSize().doubleValue())
                .blurHash(blurHash)
                .filesPaths(filePath)
                .build();

        return filesRepository.save(fileRecord);
    }

    private void createFileBucketAssociation(FilesModel file, BucketsModel bucket) {
        // Get the next order number for this file
        int nextOrder = filesBucketsRepository.findByFileId(file.getId()).size() + 1;

        FilesBucketsModel fileBucket = FilesBucketsModel.builder()
                .file(file)
                .bucket(bucket)
                .bucketOrder(nextOrder)
                .build();

        filesBucketsRepository.save(fileBucket);
    }

    private FileResponseDto buildFileResponse(FilesModel file, FilesPathsModel filePath, BucketsModel bucket) {
        String fileUrl = generateFileUrl(file.getFileKey(), filePath.getPath());

        return FileResponseDto.builder()
                .id(file.getId())
                .key(file.getFileKey())
                .name(file.getName())
                .extension(file.getExtension())
                .size(file.getSize())
                .blurHash(file.getBlurHash())
                .path(filePath != null ? filePath.getPath() : null)
                .url(fileUrl)
                .contentType(getContentTypeFromExtension("." + file.getExtension()))
                .bucket(bucket.getName())
                .createdAt(file.getCreatedAt())
                .build();
    }

    private String generateFileUrl(String fileKey, String filePath) {
        // Determine if this is an image based on the file key extension
        String endpoint = isImageFile(fileKey) ? "images" : "files";
        return String.format("http://%s:%s/api/v1/minio/%s%s", domain, serverPort, endpoint, filePath + "/" + fileKey);
    }

    /**
     * Check if a file is an image based on its extension
     */
    private boolean isImageFile(String fileKey) {
        if (fileKey == null || !fileKey.contains(".")) {
            return false;
        }

        String extension = fileKey.substring(fileKey.lastIndexOf(".")).toLowerCase();
        return extension.matches("\\.(jpg|jpeg|png|gif|bmp|webp|svg|ico)$");
    }

    private String getContentTypeFromExtension(String extension) {
        if (extension == null)
            return "application/octet-stream";

        switch (extension.toLowerCase()) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".pdf":
                return "application/pdf";
            case ".doc":
                return "application/msword";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }

    private BucketsModel getBucketForFile(Integer fileId) {
        return filesBucketsRepository.findByFileId(fileId).stream()
                .findFirst()
                .map(FilesBucketsModel::getBucket)
                .orElseThrow(() -> new RuntimeException("No bucket found for file ID: " + fileId));
    }

    private void deleteFromMinIO(String fileKey, String bucketName) {
        try {
            // Use MinioService to delete the file
            minioService.deleteFile(fileKey);
            log.info("Successfully deleted file from MinIO: {} in bucket: {}", fileKey, bucketName);
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete file from MinIO", e);
        }
    }
}