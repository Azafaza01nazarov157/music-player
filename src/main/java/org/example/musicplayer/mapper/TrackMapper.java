package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.AlbumRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.track.TrackDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TrackMapper {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "albumId", source = "album.id")
    @Mapping(target = "userId", source = "user.id")
    public abstract TrackDTO toDTO(Track track);

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    @Mapping(target = "playHistory", ignore = true)
    public abstract Track toEntity(TrackDTO trackDTO);

    @AfterMapping
    protected void afterToEntity(TrackDTO trackDTO, @MappingTarget Track track) {
        if (trackDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(trackDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found with id: " + trackDTO.getAlbumId()));
            track.setAlbum(album);
        }

        if (trackDTO.getUserId() != null) {
            User user = userRepository.findById(trackDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + trackDTO.getUserId()));
            track.setUser(user);
        }
    }

    public List<TrackDTO> toDTOList(List<Track> tracks) {
        if (tracks == null) {
            return Collections.emptyList();
        }
        return tracks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    @Mapping(target = "playHistory", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public abstract Track updateTrackFromDTO(TrackDTO trackDTO, @MappingTarget Track track);

    @AfterMapping
    protected void afterUpdateTrackFromDTO(TrackDTO trackDTO, @MappingTarget Track track) {
        if (trackDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(trackDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found with id: " + trackDTO.getAlbumId()));
            track.setAlbum(album);
        }

        if (trackDTO.getUserId() != null) {
            User user = userRepository.findById(trackDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + trackDTO.getUserId()));
            track.setUser(user);
        }
    }
}