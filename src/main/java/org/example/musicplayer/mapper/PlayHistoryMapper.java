package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.PlayHistory;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.TrackRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.playHistory.PlayHistoryDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PlayHistoryMapper {

    @Autowired
    protected UserRepository userRepository;
    
    @Autowired
    protected TrackRepository trackRepository;

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "trackId", source = "track.id")
    public abstract PlayHistoryDTO toDto(PlayHistory playHistory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "track", ignore = true)
    @Mapping(target = "playedAt", ignore = true)
    public abstract PlayHistory toEntity(PlayHistoryDTO playHistoryDTO);

    @AfterMapping
    protected void afterToEntity(PlayHistoryDTO playHistoryDTO, @MappingTarget PlayHistory playHistory) {
        if (playHistoryDTO.getUserId() != null) {
            User user = userRepository.findById(playHistoryDTO.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "User not found with id: " + playHistoryDTO.getUserId())));
            playHistory.setUser(user);
        }
        
        if (playHistoryDTO.getTrackId() != null) {
            Track track = trackRepository.findById(playHistoryDTO.getTrackId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "Track not found with id: " + playHistoryDTO.getTrackId())));
            playHistory.setTrack(track);
        }
    }

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "track", ignore = true)
    @Mapping(target = "playedAt", ignore = true)
    public abstract PlayHistory updateEntity(PlayHistoryDTO playHistoryDTO, @MappingTarget PlayHistory playHistory);

    @AfterMapping
    protected void afterUpdateEntity(PlayHistoryDTO playHistoryDTO, @MappingTarget PlayHistory playHistory) {
        if (playHistoryDTO.getUserId() != null) {
            User user = userRepository.findById(playHistoryDTO.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "User not found with id: " + playHistoryDTO.getUserId())));
            playHistory.setUser(user);
        }
        
        if (playHistoryDTO.getTrackId() != null) {
            Track track = trackRepository.findById(playHistoryDTO.getTrackId())
                    .orElseThrow(() -> new NotFoundException(
                            new ErrorDto("404", "Track not found with id: " + playHistoryDTO.getTrackId())));
            playHistory.setTrack(track);
        }
    }
}