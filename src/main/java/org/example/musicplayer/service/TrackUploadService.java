package org.example.musicplayer.service;

import org.example.musicplayer.model.TrackProcessingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackUploadService {

    private final MinioService minioService;
    private final KafkaProducerService kafkaProducerService;
    
    @Value("${spring.minio.endpoint}")
    private String minioEndpoint;
    
    /**
     * Uploads a track file to MinIO and sends a processing request to Kafka
     * 
     * @param file The audio file to upload
     * @param trackId The ID of the track
     * @param userId The ID of the user
     * @param title The title of the track
     * @param artist The artist of the track
     * @param album The album of the track (optional)
     * @param genre The genre of the track (optional)
     * @param duration The duration in seconds (optional)
     * @param releaseDate The release date in format yyyy-MM-dd (optional)
     * @param isPublic Whether the track is public
     * @return The processing request that was sent to Kafka
     */
    public TrackProcessingRequest uploadAndProcessTrack(
            MultipartFile file,
            String trackId,
            String userId,
            String title,
            String artist,
            String album,
            String genre,
            Integer duration,
            String releaseDate,
            boolean isPublic
    ) {
        try {
            // Get file information
            String originalFilename = file.getOriginalFilename();
            String fileFormat = getFileExtension(originalFilename);
            
            // Upload file to MinIO
            String objectPath = minioService.uploadFile(file, originalFilename);
            String fullPath = minioEndpoint + "/browser/" + objectPath;
            
            // Create processing request
            TrackProcessingRequest request = TrackProcessingRequest.builder()
                    .trackId(trackId)
                    .userId(userId)
                    .originalPath(fullPath)
                    .fileName(originalFilename)
                    .fileFormat(fileFormat)
                    .processingRequired(Arrays.asList("128", "256")) // Default processing options
                    .isPublic(isPublic)
                    .metadata(TrackProcessingRequest.TrackMetadata.builder()
                            .title(title)
                            .artist(artist)
                            .album(album != null ? album : "Unknown")
                            .genre(genre != null ? genre : "Unknown")
                            .duration(duration)
                            .releaseDate(releaseDate)
                            .build())
                    .build();
            
            // Send processing request to Kafka
  //          kafkaProducerService.sendTrackProcessingRequest(request);
            
            log.info("Track uploaded and processing request sent: {}", trackId);
            
            return request;
            
        } catch (Exception e) {
            log.error("Error processing track upload: {}", e.getMessage(), e);
            throw new RuntimeException("Could not process track upload", e);
        }
    }
    
    /**
     * Gets the file extension from a filename
     * 
     * @param filename The filename
     * @return The file extension
     */
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