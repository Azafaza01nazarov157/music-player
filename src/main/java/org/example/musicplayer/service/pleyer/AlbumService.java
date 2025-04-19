package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.album.AlbumDTO;
import org.example.musicplayer.dtos.album.CreateAlbumDTO;

import java.util.List;
import java.util.Optional;

public interface AlbumService {
    CreateAlbumDTO save(CreateAlbumDTO albumDTO, User currentUser);

    AlbumDTO update(Long id, AlbumDTO albumDTO);

    List<AlbumDTO> findAll();

    Optional<AlbumDTO> findById(Long id);

    List<AlbumDTO> findByUserId(Long userId);

    List<AlbumDTO> findByTitle(String title);

    List<AlbumDTO> findByUserIdAndTitle(Long userId, String title);

    void deleteById(Long id);
}