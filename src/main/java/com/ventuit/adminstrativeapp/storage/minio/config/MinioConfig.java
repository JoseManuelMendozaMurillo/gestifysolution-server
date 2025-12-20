package com.ventuit.adminstrativeapp.storage.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class MinioConfig implements CommandLineRunner {

    private final MinioProvider minioProvider;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProvider.getMinioEndpoint())
                .credentials(minioProvider.getMinioAccessKey(), minioProvider.getMinioSecretKey())
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            MinioClient minioClient = minioClient();

            // Check if bucket exists
            boolean bucketExists = minioClient
                    .bucketExists(BucketExistsArgs.builder().bucket(minioProvider.getBucketName()).build());

            if (!bucketExists) {
                // Create bucket if it doesn't exist
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProvider.getBucketName()).build());
                log.info("MinIO bucket '{}' created successfully", minioProvider.getBucketName());
            } else {
                log.info("MinIO bucket '{}' already exists", minioProvider.getBucketName());
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            // Don't throw the exception to allow the application to start even if MinIO is
            // not available
        }
    }
}