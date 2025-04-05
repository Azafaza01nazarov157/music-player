package org.example.musicplayer.dtos.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackDTO {
    private Long id;
    private String title;
    private Long artistId;
    private Long albumId;
    private Long userId;
    private String filePath;
    private Long fileSize;
    private String fileFormat;
    private BigDecimal duration;
    private Integer bitRate;
    private Integer sampleRate;
    private Integer track_number;
    private String genre;
    private Long playCount;
    private String status;
    private String processingId;
    private Set<Long> playlistIds = new HashSet<>();
}