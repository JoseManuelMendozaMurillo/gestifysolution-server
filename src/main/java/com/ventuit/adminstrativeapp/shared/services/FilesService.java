package com.ventuit.adminstrativeapp.shared.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ventuit.adminstrativeapp.minio.services.MinioService;
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
import com.ventuit.adminstrativeapp.core.services.BlurHashService;

import lombok.RequiredArgsConstructor;

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

    @Value("${app.domain}")
    private String domain;

    @Value("${app.minio.exposed-port-api}")
    private String minioPort;

    /**
     * Upload a file to storage and register it in the database
     * 
     * @param uploadDto File upload data
     * @return File response with metadata and URL
     */
    public FileResponseDto uploadFile(FileUploadDto uploadDto) {
        String fileKey = null;
        String bucketName = null;
        try {
            // Generate unique file key
            fileKey = generateFileKey(uploadDto.getOriginalFileName());

            // Get file extension
            String extension = getFileExtension(uploadDto.getOriginalFileName());

            // Get or create file path record
            FilesPathsModel filePath = getOrCreateFilePath(uploadDto.getPath());

            // Get bucket from database
            BucketsModel bucket = getBucketFromStorage(uploadDto.getBucket(), uploadDto.getProvider());
            bucketName = bucket.getName();

            // Upload file to MinIO using MinioService
            uploadToMinIO(uploadDto, fileKey, bucketName);

            // Always generate blurhash if image
            String blurHash = null;
            if (uploadDto.getContentType() != null && uploadDto.getContentType().startsWith("image/")) {
                uploadDto.getFileInputStream().reset();
                blurHash = blurHashService.generateBlurHash(uploadDto.getFileInputStream(), 4, 3);
            }

            // Create file record in database
            FilesModel fileRecord = createFileRecord(uploadDto, fileKey, extension, filePath, blurHash);

            // Create file-bucket association
            createFileBucketAssociation(fileRecord, bucket);

            // Generate response
            return buildFileResponse(fileRecord, filePath, bucket);

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
            throw new RuntimeException("Failed to upload file: " + uploadDto.getOriginalFileName(), e);
        }
    }

    /**
     * Delete a file from storage and database
     * 
     * @param fileId File ID to delete
     * @return true if deleted successfully
     */
    public boolean deleteFile(Integer fileId) {
        try {
            FilesModel file = filesRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

            // Get bucket information
            BucketsModel bucket = getBucketForFile(fileId);

            // Delete file-bucket associations
            filesBucketsRepository.findByFileId(fileId)
                    .forEach(filesBucketsRepository::delete);

            // Delete file record
            filesRepository.delete(file);

            // Delete from MinIO using MinioService
            deleteFromMinIO(file.getFileKey(), bucket.getName());

            log.info("Successfully deleted file: {} (ID: {})", file.getName(), fileId);
            return true;
        } catch (Exception e) {
            log.error("Error deleting file with ID: {}", fileId, e);
            throw new RuntimeException("Failed to delete file with ID: " + fileId, e);
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
            throw new RuntimeException("Failed to get file with ID: " + fileId, e);
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
            throw new RuntimeException("Failed to get file with key: " + key, e);
        }
    }

    // Private helper methods

    private String generateFileKey(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = getFileExtension(originalFileName);
        return timestamp + "_" + uuid + extension;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
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

    private void uploadToMinIO(FileUploadDto uploadDto, String fileKey, String bucketName) {
        try {
            // Use MinioService to upload the file with InputStream
            minioService.uploadFile(fileKey, uploadDto.getFileInputStream(), uploadDto.getFileSize(),
                    uploadDto.getContentType());
            log.info("Successfully uploaded file to MinIO: {} in bucket: {}", fileKey, bucketName);
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    private FilesModel createFileRecord(FileUploadDto uploadDto, String fileKey, String extension,
            FilesPathsModel filePath, String blurHash) {
        FilesModel fileRecord = FilesModel.builder()
                .fileKey(fileKey)
                .name(uploadDto.getOriginalFileName())
                .extension(extension)
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
        String fileUrl = generateFileUrl(file.getFileKey(), bucket);

        return FileResponseDto.builder()
                .id(file.getId())
                .key(file.getFileKey())
                .name(file.getName())
                .extension(file.getExtension())
                .size(file.getSize())
                .blurHash(file.getBlurHash())
                .path(filePath != null ? filePath.getPath() : null)
                .url(fileUrl)
                .contentType(getContentTypeFromExtension(file.getExtension()))
                .bucket(bucket.getName())
                .createdAt(file.getCreatedAt())
                .build();
    }

    private String generateFileUrl(String fileKey, BucketsModel bucket) {
        return String.format("http://%s:%s/%s/%s", domain, minioPort, bucket.getName(), fileKey);
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