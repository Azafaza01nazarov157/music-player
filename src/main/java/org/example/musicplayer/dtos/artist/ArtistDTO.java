package org.example.musicplayer.dtos.artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Long id;
    private String name;
    private String biography;
    private String imageUrl;
    private Set<Long> albumIds = new HashSet<>();
    private Set<Long> trackIds = new HashSet<>();
}