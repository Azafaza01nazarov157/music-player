package org.example.musicplayer.dtos.playlist;

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
public class PlaylistDTO {
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private String coverUrl;
    private Boolean isPublic;
    private Set<Long> trackIds = new HashSet<>();
}
