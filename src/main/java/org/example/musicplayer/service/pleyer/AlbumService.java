package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.dtos.album.AlbumDTO;

import java.util.List;
import java.util.Optional;

public interface AlbumService {
    AlbumDTO save(AlbumDTO albumDTO);

    AlbumDTO update(Long id, AlbumDTO albumDTO);

    List<AlbumDTO> findAll();

    Optional<AlbumDTO> findById(Long id);

    List<AlbumDTO> findByArtistId(Long artistId);

    List<AlbumDTO> findByTitle(String title);

    List<AlbumDTO> findByArtistIdAndTitle(Long artistId, String title);

    void deleteById(Long id);
}