package com.ventuit.adminstrativeapp.storage.minio.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import com.ventuit.adminstrativeapp.shared.exceptions.FileUploadException;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.storage.dto.FileProxyResponse;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;
import com.ventuit.adminstrativeapp.shared.exceptions.FileGetException;
import com.ventuit.adminstrativeapp.shared.exceptions.FileDeleteException;

import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProvider minioProvider;

    /**
     * Upload a file to MinIO using InputStream
     */
    public String uploadFile(String objectName, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build());
            log.info("File uploaded successfully: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new FileUploadException("Failed to upload file", e);
        }
    }

    /**
     * Upload a file to MinIO
     */
    public String uploadFile(String objectName, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            log.info("File uploaded successfully: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new FileUploadException("Failed to upload file", e);
        }
    }

    /**
     * Download a file from MinIO
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
            throw new FileGetException("Failed to download file", e);
        }
    }

    /**
     * Delete a file from MinIO
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .build());
            log.info("File deleted successfully: {}", objectName);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
            throw new FileDeleteException("Failed to delete file", e);
        }
    }

    public void deleteFile(FilesModel file) {
        if (file == null)
            return;

        String objectName = "";
        if (file.getFilesPaths() != null) {
            objectName = file.getFilesPaths().getPath() + "/";
        }
        objectName += file.getFileKey();
        deleteFile(objectName);
    }

    /**
     * Check if a file exists in MinIO
     */
    public boolean fileExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .build());
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new FileGetException("Error checking file existence", e);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", e.getMessage(), e);
            throw new FileGetException("Failed to check file existence", e);
        }
    }

    /**
     * Get file metadata from MinIO
     */
    public StatObjectResponse getFileMetadata(String objectName) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error getting file metadata: {}", e.getMessage(), e);
            throw new FileGetException("Failed to get file metadata", e);
        }
    }

    /**
     * Serve file as proxy from MinIO
     * This method returns the file stream and metadata for serving through the
     * server
     */
    public FileProxyResponse serveFile(String objectName) {
        try {
            // Get file metadata first
            StatObjectResponse metadata = getFileMetadata(objectName);

            // Get file stream
            InputStream fileStream = downloadFile(objectName);

            return FileProxyResponse.builder()
                    .inputStream(fileStream)
                    .contentType(metadata.contentType())
                    .contentLength(metadata.size())
                    .lastModified(metadata.lastModified())
                    .etag(metadata.etag())
                    .build();
        } catch (Exception e) {
            log.error("Error serving file: {}", e.getMessage(), e);
            throw new FileGetException("Failed to serve file", e);
        }
    }

    /**
     * Check if file is an image based on content type
     */
    public boolean isImage(String objectName) {
        try {
            StatObjectResponse metadata = getFileMetadata(objectName);
            String contentType = metadata.contentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            log.error("Error checking if file is image: {}", e.getMessage(), e);
            return false;
        }
    }

    public void clearBucket() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProvider.getBucketName())
                            .recursive(true)
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProvider.getBucketName())
                                .object(item.objectName())
                                .build());
                log.info("Deleted object: {}", item.objectName());
            }
            log.info("Bucket cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing bucket: {}", e.getMessage(), e);
            throw new FileDeleteException("Failed to clear bucket", e);
        }
    }
}