package org.example.musicplayer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.model.TrackProcessingDto;
import org.example.musicplayer.model.TrackProcessingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackUploadService {

    private final MinioService minioService;
    private final KafkaProducerService kafkaProducerService;

    @Value("${spring.minio.endpoint}")
    private String minioEndpoint;

    public TrackProcessingDto uploadAndProcessTrack(MultipartFile file, String trackId, String userId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileFormat = getFileExtension(originalFilename);

            String objectPath = minioService.uploadFile(file, originalFilename, userId);

            TrackProcessingDto request = new TrackProcessingDto();
            request.setTrackId(trackId);
            request.setUserId(userId);
//            request.setAlbumId();
            request.setFilePath(objectPath);
            request.setFileName(originalFilename);
            request.setFileFormat(fileFormat);

            kafkaProducerService.sendTrackProcessingRequest(request);

            log.info("Track uploaded and processing request sent: {}", trackId);

            return request;

        } catch (Exception e) {
            log.error("Error processing track upload: {}", e.getMessage(), e);
            throw new RuntimeException("Could not process track upload", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
} 