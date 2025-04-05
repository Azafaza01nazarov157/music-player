package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.dtos.artist.ArtistDTO;

import java.util.List;
import java.util.Optional;

public interface ArtistService {
    ArtistDTO save(ArtistDTO artistDTO);

    ArtistDTO update(Long id, ArtistDTO artistDTO);

    List<ArtistDTO> findAll();

    Optional<ArtistDTO> findById(Long id);

    List<ArtistDTO> findByName(String name);

    void deleteById(Long id);

    boolean existsByName(String name);
}