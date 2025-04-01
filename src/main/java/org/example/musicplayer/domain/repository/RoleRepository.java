package org.example.musicplayer.domain.repository;

import org.example.musicplayer.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findTopByName(String user);
}