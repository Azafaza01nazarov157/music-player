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
public class StreamSessionMessage {
    private Long id;
    private Long userId;
    private Long trackId;
    private String sessionId;
    private String quality;
    private Double currentPos;
    private Integer bufferSize;
    private Boolean isActive;
    private String ipAddress;
    private String userAgent;
    private OffsetDateTime startedAt;
    private OffsetDateTime lastAccessAt;
    private OffsetDateTime endedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
