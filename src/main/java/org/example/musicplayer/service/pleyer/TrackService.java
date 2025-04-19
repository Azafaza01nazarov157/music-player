package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.dtos.track.TrackDTO;

import java.util.List;
import java.util.Optional;

public interface TrackService {

    TrackDTO save(TrackDTO trackDTO);

    TrackDTO updateTrackStatus(Long id, String status);

    List<TrackDTO> findAll();

    Optional<TrackDTO> findById(Long id);

    List<TrackDTO> findByTitle(String title);

    List<TrackDTO> findByUserId(Long userId);

    List<TrackDTO> findByAlbumId(Long albumId);

    List<TrackDTO> findByStatus(String status);

    TrackDTO update(Long id, TrackDTO trackDTO);

    void deleteById(Long id);

    void incrementPlayCount(Long id);
}