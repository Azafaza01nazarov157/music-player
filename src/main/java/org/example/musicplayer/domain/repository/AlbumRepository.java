package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtistId(Long artistId);

    List<Album> findByTitleContainingIgnoreCase(String title);

    List<Album> findByArtistIdAndTitleContainingIgnoreCase(Long artistId, String title);
}