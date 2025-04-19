package org.example.musicplayer.dtos.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlbumDTO {
    private String title;
    private LocalDate releaseDate;
    private String coverUrl;
    private String genre;
    private String description;
}
