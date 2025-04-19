package org.example.musicplayer.dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackMessage {
    private Long id;
    private String title;
    private Long artistId;
    private Long albumId;
    private Long userId;
    private String filePath;
    private Long fileSize;
    private String fileFormat;
    private Double duration;
    private Integer bitRate;
    private Integer sampleRate;
    private Integer trackNumber;
    private String genre;
    private Long playCount;
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;
}
