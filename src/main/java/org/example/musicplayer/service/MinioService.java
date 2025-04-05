package org.example.musicplayer.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${spring.minio.bucketName}")
    private String bucketName;

    /**
     * Initializes the MinIO bucket if it doesn't exist
     */
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created MinIO bucket: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize MinIO storage", e);
        }
    }

    /**
     * Uploads a file to MinIO storage
     * 
     * @param file The file to upload
     * @param fileName Optional filename (if null, will use original filename)
     * @return The object path in MinIO
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            if (fileName == null) {
                fileName = file.getOriginalFilename();
            }
            
            // Ensure the bucket exists
            init();
            
            // Create a unique object name if needed
            String objectName = UUID.randomUUID() + "-" + fileName;
            
            // Upload the file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            
            log.info("File uploaded successfully to MinIO: {}", objectName);
            
            // Return the complete path to the file
            return bucketName + "/" + objectName;
            
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Could not upload file to MinIO", e);
        }
    }
    
    /**
     * Gets a presigned URL for an object
     * 
     * @param objectName The name of the object
     * @return The presigned URL
     */
    public String getPresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage(), e);
            throw new RuntimeException("Could not generate presigned URL", e);
        }
    }
} 