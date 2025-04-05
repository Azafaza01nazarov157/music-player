package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Playlist;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.TrackRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.playlist.PlaylistDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PlaylistMapper {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "trackIds", ignore = true)
    public abstract PlaylistDTO toDTO(Playlist playlist);

    @AfterMapping
    protected void afterToDTO(Playlist playlist, @MappingTarget PlaylistDTO playlistDTO) {
        if (playlist.getTracks() != null) {
            playlistDTO.setTrackIds(playlist.getTracks().stream()
                    .map(Track::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    public abstract Playlist toEntity(PlaylistDTO playlistDTO);

    @AfterMapping
    protected void afterToEntity(PlaylistDTO playlistDTO, @MappingTarget Playlist playlist) {
        if (playlistDTO.getUserId() != null) {
            User user = userRepository.findById(playlistDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + playlistDTO.getUserId()));
            playlist.setUser(user);
        }

        if (playlistDTO.getTrackIds() != null && !playlistDTO.getTrackIds().isEmpty()) {
            Set<Track> tracks = new HashSet<>(trackRepository.findAllById(playlistDTO.getTrackIds()));
            if (tracks.size() != playlistDTO.getTrackIds().size()) {
                throw new RuntimeException("One or more tracks not found");
            }
            playlist.setTracks(tracks);
        } else {
            playlist.setTracks(new HashSet<>());
        }
    }

    public List<PlaylistDTO> toDTOList(List<Playlist> playlists) {
        if (playlists == null) {
            return Collections.emptyList();
        }
        return playlists.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public abstract Playlist updatePlaylistFromDTO(PlaylistDTO playlistDTO, @MappingTarget Playlist playlist);

    @AfterMapping
    protected void afterUpdatePlaylistFromDTO(PlaylistDTO playlistDTO, @MappingTarget Playlist playlist) {
        if (playlistDTO.getTrackIds() != null) {
            Set<Track> tracks = new HashSet<>(trackRepository.findAllById(playlistDTO.getTrackIds()));
            if (tracks.size() != playlistDTO.getTrackIds().size()) {
                throw new RuntimeException("One or more tracks not found");
            }
            playlist.setTracks(tracks);
        }

        if (playlistDTO.getUserId() != null) {
            User user = userRepository.findById(playlistDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + playlistDTO.getUserId()));
            playlist.setUser(user);
        }
    }
}