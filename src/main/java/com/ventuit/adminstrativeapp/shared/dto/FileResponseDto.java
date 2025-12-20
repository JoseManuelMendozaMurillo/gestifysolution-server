package com.ventuit.adminstrativeapp.shared.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for file upload responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {

    /**
     * File ID in the database
     */
    private Integer id;

    /**
     * File key in storage
     */
    private String key;

    /**
     * Original file name
     */
    private String name;

    /**
     * File extension
     */
    private String extension;

    /**
     * File size in bytes
     */
    private Double size;

    /**
     * Blur hash for images
     */
    private String blurHash;

    /**
     * File path in storage
     */
    private String path;

    /**
     * Direct URL to access the file
     */
    private String url;

    /**
     * Content type (MIME type)
     */
    private String contentType;

    /**
     * Bucket where the file is stored
     */
    private String bucket;

    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;
}