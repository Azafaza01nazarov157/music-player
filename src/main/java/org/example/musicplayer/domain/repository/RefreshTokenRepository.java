package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.RefreshToken;
import org.example.musicplayer.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = "user")
    RefreshToken findByTokenAndExpirationTimeAfter(String token, OffsetDateTime expirationTime);

    RefreshToken findFirstByUser(User user);

}