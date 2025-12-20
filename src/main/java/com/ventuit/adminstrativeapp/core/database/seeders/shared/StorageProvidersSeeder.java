package com.ventuit.adminstrativeapp.core.database.seeders.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.shared.models.StorageProvidersModel;
import com.ventuit.adminstrativeapp.shared.repositories.StorageProvidersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Order(2)
public class StorageProvidersSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StorageProvidersSeeder.class);
    private static final String DEFAULT_CREATED_BY = "Gestify solution server";

    private final StorageProvidersRepository storageProvidersRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (storageProvidersRepository.count() == 0) {
                fillStorageProvidersTable();
            }
        } catch (Exception error) {
            log.error("An error has occurred when inserting data into the storage_providers table", error);
        }
    }

    private void fillStorageProvidersTable() {
        StorageProvidersModel minioProvider = StorageProvidersModel.builder()
                .provider("minio")
                .createdBy(DEFAULT_CREATED_BY)
                .build();
        storageProvidersRepository.save(minioProvider);
        log.info("Successfully inserted MinIO storage provider into the storage_providers table");
    }
}