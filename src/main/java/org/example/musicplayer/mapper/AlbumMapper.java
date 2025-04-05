package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.entity.Artist;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.repository.ArtistRepository;
import org.example.musicplayer.domain.repository.TrackRepository;
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
    protected ArtistRepository artistRepository;

    @Autowired
    protected TrackRepository trackRepository;

    @Mapping(target = "artistId", source = "artist.id")
    @Mapping(target = "trackIds", ignore = true)
    public abstract AlbumDTO toDto(Album album);

    @AfterMapping
    protected void afterToDto(Album album, @MappingTarget AlbumDTO albumDTO) {
        albumDTO.setTrackIds(album.getTracks().stream()
                .map(Track::getId)
                .collect(Collectors.toSet()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    public abstract Album toEntity(AlbumDTO albumDTO);

    @AfterMapping
    protected void afterToEntity(AlbumDTO albumDTO, @MappingTarget Album album) {
        if (albumDTO.getArtistId() != null) {
            Artist artist = artistRepository.findById(albumDTO.getArtistId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "Artist not found with id: " + albumDTO.getArtistId())));
            album.setArtist(artist);
        }
    }

    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "artist", ignore = true)
    public abstract Album updateEntity(AlbumDTO albumDTO, @MappingTarget Album album);

    @AfterMapping
    protected void afterUpdateEntity(AlbumDTO albumDTO, @MappingTarget Album album) {
        if (albumDTO.getArtistId() != null) {
            Artist artist = artistRepository.findById(albumDTO.getArtistId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "Artist not found with id: " + albumDTO.getArtistId())));
            album.setArtist(artist);
        }

        if (albumDTO.getTrackIds() != null && !albumDTO.getTrackIds().isEmpty()) {
            List<Track> tracks = trackRepository.findAllById(albumDTO.getTrackIds());
            if (tracks.size() != albumDTO.getTrackIds().size()) {
                throw new NotFoundException(
                        new ErrorDto("404", "One or more tracks not found"));
            }
            album.setTracks(new HashSet<>(tracks));
        }
    }
}
