package org.example.musicplayer.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${spring.minio.endpoint}")
    private String endpoint;

    @Value("${spring.minio.accessKey}")
    private String accessKey;

    @Value("${spring.minio.secretKey}")
    private String secretKey;

    @Value("${spring.minio.secure}")
    private boolean secure;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
} 