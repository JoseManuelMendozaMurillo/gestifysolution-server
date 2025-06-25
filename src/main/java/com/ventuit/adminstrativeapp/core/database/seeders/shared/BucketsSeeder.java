package com.ventuit.adminstrativeapp.core.database.seeders.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import com.ventuit.adminstrativeapp.shared.models.BucketsModel;
import com.ventuit.adminstrativeapp.shared.models.StorageProvidersModel;
import com.ventuit.adminstrativeapp.shared.repositories.BucketsRepository;
import com.ventuit.adminstrativeapp.shared.repositories.StorageProvidersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Order(3)
public class BucketsSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BucketsSeeder.class);
    private static final String DEFAULT_CREATED_BY = "Gestify solution server";

    private final BucketsRepository bucketsRepository;
    private final StorageProvidersRepository storageProvidersRepository;
    private final MinioClient minioClient;

    @Value("${app.minio.bucket-name}")
    private String minioBucketName;

    @Value("${app.minio.exposed-port-api}")
    private String minioPort;

    @Value("${app.domain}")
    private String domain;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (bucketsRepository.count() == 0) {
                fillBucketsTable();
            }
        } catch (Exception error) {
            log.error("An error has occurred when inserting data into the buckets table", error);
        }
    }

    private void fillBucketsTable() {
        StorageProvidersModel minioProvider = storageProvidersRepository.findAll().stream()
                .filter(sp -> sp.getProvider().equalsIgnoreCase("minio"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Minio storage provider not found"));

        // Check if the MinIO bucket has been created by MinioConfig
        try {
            log.info("Checking MinIO bucket existence: {}", minioBucketName);

            boolean bucketExists = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder().bucket(minioBucketName).build());

            if (!bucketExists) {
                log.warn("MinIO bucket '{}' has not been created yet. Skipping bucket record creation.",
                        minioBucketName);
                return;
            }
        } catch (Exception e) {
            log.error("Error checking MinIO bucket existence: {}", e.getMessage(), e);
            log.warn("Skipping bucket record creation due to MinIO connection issues.");
            return;
        }

        String url = String.format("http://%s:%s/%s", domain, minioPort, minioBucketName);

        BucketsModel minioBucket = BucketsModel.builder()
                .name(minioBucketName)
                .url(url)
                .storageProvider(minioProvider)
                .createdBy(DEFAULT_CREATED_BY)
                .build();
        bucketsRepository.save(minioBucket);
        log.info("Successfully inserted MinIO bucket '{}' into the buckets table", minioBucketName);
    }
}