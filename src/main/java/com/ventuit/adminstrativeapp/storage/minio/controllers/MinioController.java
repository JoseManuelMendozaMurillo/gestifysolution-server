package com.ventuit.adminstrativeapp.storage.minio.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.util.AntPathMatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ventuit.adminstrativeapp.storage.dto.FileMetadataResponse;
import com.ventuit.adminstrativeapp.storage.dto.FileProxyResponse;
import com.ventuit.adminstrativeapp.storage.minio.services.MinioService;

import io.minio.StatObjectResponse;

import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/minio")
@Slf4j
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String uploadedFileName = minioService.uploadFile(fileName, file);
            return ResponseEntity.ok("File uploaded successfully: " + uploadedFileName);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        try {
            InputStream inputStream = minioService.downloadFile(fileName);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            minioService.deleteFile(fileName);
            return ResponseEntity.ok("File deleted successfully: " + fileName);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to delete file: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{fileName}")
    public ResponseEntity<Boolean> fileExists(@PathVariable String fileName) {
        try {
            boolean exists = minioService.fileExists(fileName);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Proxy endpoint to serve images from MinIO
     * This endpoint acts as a proxy between the client and the private MinIO bucket
     */
    @GetMapping("/images/**")
    public void serveImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract fileName from the request path
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            AntPathMatcher apm = new AntPathMatcher();
            String fileName = apm.extractPathWithinPattern(bestMatchPattern, path);

            // Check if file exists
            if (!minioService.fileExists(fileName)) {
                log.warn("Image file not found: {}", fileName);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Check if file is actually an image
            if (!minioService.isImage(fileName)) {
                log.warn("File is not an image: {}", fileName);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Get file through proxy service
            FileProxyResponse proxyResponse = minioService.serveFile(fileName);
            response.setContentType(proxyResponse.getContentType());
            response.setContentLengthLong(proxyResponse.getContentLength());

            // Add caching headers
            if (proxyResponse.getLastModified() != null) {
                response.setDateHeader("Last-Modified", proxyResponse.getLastModified().toInstant().toEpochMilli());
            }

            if (proxyResponse.getEtag() != null) {
                String etag = proxyResponse.getEtag();
                if (!etag.startsWith("W/") && !etag.startsWith("\"")) {
                    etag = "\"" + etag + "\"";
                }
                response.setHeader("ETag", etag);
            }

            // Add cache control headers for images
            response.setHeader("Cache-Control", "public, max-age=31536000"); // 1 year cache for images

            try (InputStream in = proxyResponse.getInputStream();
                    OutputStream out = response.getOutputStream()) {
                in.transferTo(out);
            }
        } catch (Exception e) {
            log.error("Error serving image: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Proxy endpoint to serve any file from MinIO
     * This endpoint acts as a proxy between the client and the private MinIO bucket
     */
    @GetMapping("/files/**")
    public void serveFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract fileName from the request path
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            AntPathMatcher apm = new AntPathMatcher();
            String fileName = apm.extractPathWithinPattern(bestMatchPattern, path);

            // Check if file exists
            if (!minioService.fileExists(fileName)) {
                log.warn("File not found: {}", fileName);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Get file through proxy service
            FileProxyResponse proxyResponse = minioService.serveFile(fileName);
            response.setContentType(proxyResponse.getContentType());
            response.setContentLengthLong(proxyResponse.getContentLength());

            // Set up headers for file serving
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(proxyResponse.getContentType()));
            headers.setContentLength(proxyResponse.getContentLength());

            // Add caching headers
            if (proxyResponse.getLastModified() != null) {
                headers.setLastModified(proxyResponse.getLastModified().toInstant().toEpochMilli());
            }

            if (proxyResponse.getEtag() != null) {
                String etag = proxyResponse.getEtag();
                if (!etag.startsWith("W/") && !etag.startsWith("\"")) {
                    etag = "\"" + etag + "\"";
                }
                headers.setETag(etag);
            }

            // Add cache control headers for files
            headers.setCacheControl("public, max-age=86400"); // 1 day cache for files

            try (InputStream in = proxyResponse.getInputStream();
                    OutputStream out = response.getOutputStream()) {
                in.transferTo(out);
            }
        } catch (Exception e) {
            log.error("Error serving file: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get file metadata without downloading the file
     */
    @GetMapping("/metadata/**")
    public ResponseEntity<Object> getFileMetadata(HttpServletRequest request) {
        try {
            // Extract fileName from the request path
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            AntPathMatcher apm = new AntPathMatcher();
            String fileName = apm.extractPathWithinPattern(bestMatchPattern, path);

            // Check if file exists
            if (!minioService.fileExists(fileName)) {
                log.warn("File not found: {}", fileName);
                return ResponseEntity.notFound().build();
            }

            // Get file metadata
            StatObjectResponse metadata = minioService.getFileMetadata(fileName);

            // Create metadata response object
            FileMetadataResponse metadataResponse = new FileMetadataResponse(
                    fileName,
                    metadata.contentType(),
                    metadata.size(),
                    metadata.lastModified(),
                    metadata.etag(),
                    minioService.isImage(fileName));

            return ResponseEntity.ok(metadataResponse);
        } catch (Exception e) {
            log.error("Error getting file metadata: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}