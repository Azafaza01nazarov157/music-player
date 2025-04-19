package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.TrackRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.album.AlbumDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AlbumMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TrackRepository trackRepository;

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "trackIds", ignore = true)
    public abstract AlbumDTO toDto(Album album);

    @AfterMapping
    protected void afterToDto(Album album, @MappingTarget AlbumDTO albumDTO) {
        albumDTO.setTrackIds(album.getTracks().stream()
                .map(Track::getId)
                .collect(Collectors.toSet()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    public abstract Album toEntity(AlbumDTO albumDTO);

    @AfterMapping
    protected void afterToEntity(AlbumDTO albumDTO, @MappingTarget Album album) {
        if (albumDTO.getUserId() != null) {
            User user = userRepository.findById(albumDTO.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "User not found with id: " + albumDTO.getUserId())));
            album.setUser(user);
        }
    }

    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Album updateEntity(AlbumDTO albumDTO, @MappingTarget Album album);

    @AfterMapping
    protected void afterUpdateEntity(AlbumDTO albumDTO, @MappingTarget Album album) {
        if (albumDTO.getUserId() != null && (album.getUser() == null || !album.getUser().getId().equals(albumDTO.getUserId()))) {
            User user = userRepository.findById(albumDTO.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "User not found with id: " + albumDTO.getUserId())));
            album.setUser(user);
        }
    }
}
