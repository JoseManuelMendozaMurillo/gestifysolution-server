package com.ventuit.adminstrativeapp.shared.dto;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for file upload requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDto {

    /**
     * File input stream
     */
    private InputStream fileInputStream;

    /**
     * Original file name
     */
    private String originalFileName;

    /**
     * File content type (MIME type)
     */
    private String contentType;

    /**
     * File size in bytes
     */
    private Long fileSize;

    /**
     * Optional path where the file should be stored
     */
    private String path;

    /**
     * Storage bucket to use for this file
     */
    private String bucket;

    /**
     * Storage provider to use for this file
     */
    private String provider;
}