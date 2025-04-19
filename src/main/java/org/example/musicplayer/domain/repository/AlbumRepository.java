package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByUserId(Long userId);

    List<Album> findByTitleContainingIgnoreCase(String title);

    List<Album> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
}