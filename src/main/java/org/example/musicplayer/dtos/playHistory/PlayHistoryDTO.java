package org.example.musicplayer.dtos.playHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayHistoryDTO {
    private Long id;
    private Long userId;
    private Long trackId;
    private LocalDateTime playedAt;
    private Long playDuration;
    private Boolean completed;
    private String ipAddress;
    private String userAgent;
}
