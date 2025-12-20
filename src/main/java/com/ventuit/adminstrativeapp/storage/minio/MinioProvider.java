package com.ventuit.adminstrativeapp.storage.minio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class MinioProvider {

    @Value("${app.minio.endpoint}")
    private String minioEndpoint;

    @Value("${app.minio.access-key}")
    private String minioAccessKey;

    @Value("${app.minio.secret-key}")
    private String minioSecretKey;

    @Value("${app.minio.bucket-name}")
    private String bucketName;
}
