package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserId(Long userId);

    List<Playlist> findByNameContainingIgnoreCase(String name);

    List<Playlist> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);

    List<Playlist> findAll();

    List<Playlist> findPublicByNameContainingIgnoreCase(String name);
}