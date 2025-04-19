package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByTitleContainingIgnoreCase(String title);

    List<Track> findByUserId(Long userId);

    List<Track> findByAlbumId(Long albumId);

    List<Track> findByStatus(String status);
}