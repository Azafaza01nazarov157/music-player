package org.example.musicplayer.service.pleyer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.repository.TrackRepository;
import org.example.musicplayer.dtos.track.TrackDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.BadRequestException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.TrackMapper;
import org.example.musicplayer.service.pleyer.TrackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    @Override
    public TrackDTO save(TrackDTO trackDTO) {
        log.info("Saving track: {}", trackDTO.getTitle());

        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);

        return trackMapper.toDTO(track);
    }

    @Override
    public TrackDTO updateTrackStatus(Long id, String status) {
        log.info("Updating track status to {} for track id: {}", status, id);

        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Track not found with id: " + id)));

        track.setStatus(status);
        track = trackRepository.save(track);

        return trackMapper.toDTO(track);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> findAll() {
        log.info("Fetching all tracks");
        List<Track> tracks = trackRepository.findAll();
        return trackMapper.toDTOList(tracks);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrackDTO> findById(Long id) {
        log.info("Fetching track with id: {}", id);
        return trackRepository.findById(id)
                .map(trackMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> findByTitle(String title) {
        log.info("Fetching tracks with title containing: {}", title);
        List<Track> tracks = trackRepository.findByTitleContainingIgnoreCase(title);
        return trackMapper.toDTOList(tracks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> findByArtistId(Long artistId) {
        log.info("Fetching tracks for artist with id: {}", artistId);
        List<Track> tracks = trackRepository.findByArtistId(artistId);
        return trackMapper.toDTOList(tracks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> findByAlbumId(Long albumId) {
        log.info("Fetching tracks for album with id: {}", albumId);
        List<Track> tracks = trackRepository.findByAlbumId(albumId);
        return trackMapper.toDTOList(tracks);
    }


    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> findByStatus(String status) {
        log.info("Fetching tracks with status: {}", status);
        List<Track> tracks = trackRepository.findByStatus(status);
        return trackMapper.toDTOList(tracks);
    }

    @Override
    public TrackDTO update(Long id, TrackDTO trackDTO) {
        log.info("Updating track with id: {}", id);

        Track existingTrack = trackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Track not found with id: " + id)));

        if (trackDTO.getId() != null && !trackDTO.getId().equals(id)) {
            throw new BadRequestException(new ErrorDto("400", "ID in the request body does not match the path parameter"));
        }

        trackDTO.setId(id);
        Track updatedTrack = trackMapper.updateTrackFromDTO(trackDTO, existingTrack);
        updatedTrack = trackRepository.save(updatedTrack);

        return trackMapper.toDTO(updatedTrack);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting track with id: {}", id);
        if (!trackRepository.existsById(id)) {
            throw new NotFoundException(new ErrorDto("404", "Track not found with id: " + id));
        }
        trackRepository.deleteById(id);
    }

    @Override
    public void incrementPlayCount(Long id) {
        log.info("Incrementing play count for track id: {}", id);

        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Track not found with id: " + id)));

        if (track.getPlayCount() == null) {
            track.setPlayCount(1L);
        } else {
            track.setPlayCount(track.getPlayCount() + 1);
        }

        trackRepository.save(track);
    }
}