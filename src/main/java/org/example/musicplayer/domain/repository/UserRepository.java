package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Role;
import org.example.musicplayer.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roles")
    User findByEmailIgnoreCase(String email);

    User findByPasswordResetKey(UUID passwordResetKey);

    Page<User> findAllById(Long id, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    List<User> findAllByRoles(Role role);

    User findByAuthenticationKey(UUID uuid);

    Optional<User> findByEmail(String email);
}