package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.entity.Artist;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.dtos.artist.ArtistDTO;
import org.mapstruct.*;

import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ArtistMapper {

    @Mapping(target = "albumIds", ignore = true)
    @Mapping(target = "trackIds", ignore = true)
    public abstract ArtistDTO toDto(Artist artist);

    @AfterMapping
    protected void afterToDto(Artist artist, @MappingTarget ArtistDTO artistDTO) {
        artistDTO.setAlbumIds(artist.getAlbums().stream()
                .map(Album::getId)
                .collect(Collectors.toSet()));

        artistDTO.setTrackIds(artist.getTracks().stream()
                .map(Track::getId)
                .collect(Collectors.toSet()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "albums", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    public abstract Artist toEntity(ArtistDTO artistDTO);

    @Mapping(target = "albums", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    public abstract Artist updateEntity(ArtistDTO artistDTO, @MappingTarget Artist artist);
}