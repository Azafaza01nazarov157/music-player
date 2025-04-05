package org.example.musicplayer.dtos.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Long id;
    private String title;
    private Long artistId;
    private LocalDate releaseDate;
    private String coverUrl;
    private String genre;
    private String description;
    private Set<Long> trackIds = new HashSet<>();
}