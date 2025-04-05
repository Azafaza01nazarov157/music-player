package org.example.musicplayer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackProcessingRequest {
    
    private String trackId;
    private String userId;
    private String originalPath;
    private String fileName;
    private String fileFormat;
    private List<String> processingRequired;
    private boolean isPublic;
    private TrackMetadata metadata;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrackMetadata {
        private String title;
        private String artist;
        private String album;
        private String genre;
        private Integer duration;
        private String releaseDate;
    }
} 