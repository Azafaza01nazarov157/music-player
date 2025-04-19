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
public class AlbumMessage {
    private Long id;
    private String title;
    private Long userId;
    private String releaseDate;
    private String coverUrl;
    private String genre;
    private String description;
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;
}
