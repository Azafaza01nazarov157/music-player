package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findTopByName(String user);

    Set<Role> findByName(String artist);
}