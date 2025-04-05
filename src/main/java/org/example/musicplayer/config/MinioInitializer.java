package org.example.musicplayer.config;

import org.example.musicplayer.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioInitializer {

    private final MinioService minioService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeMinIO() {
        try {
            log.info("Initializing MinIO storage...");
            minioService.init();
            log.info("MinIO storage initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize MinIO storage: {}", e.getMessage(), e);
        }
    }
} 