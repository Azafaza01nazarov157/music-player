package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.dtos.playlist.PlaylistDTO;

import java.util.List;
import java.util.Optional;


public interface PlaylistService {

    PlaylistDTO save(PlaylistDTO playlistDTO);

    List<PlaylistDTO> findAll();

    Optional<PlaylistDTO> findById(Long id);

    List<PlaylistDTO> findByUserId(Long userId);

    List<PlaylistDTO> findByName(String name);

    List<PlaylistDTO> findByUserIdAndName(Long userId, String name);

    List<PlaylistDTO> findAllPublic();

    List<PlaylistDTO> findPublicByName(String name);

    PlaylistDTO addTrackToPlaylist(Long playlistId, Long trackId);

    PlaylistDTO removeTrackFromPlaylist(Long playlistId, Long trackId);

    PlaylistDTO update(Long id, PlaylistDTO playlistDTO);

    void deleteById(Long id);
}