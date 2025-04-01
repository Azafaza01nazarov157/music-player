package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
}