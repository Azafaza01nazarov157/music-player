package org.example.musicplayer.service.user;

import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.user.RoleDTO;

import java.util.List;


public interface RoleService {
    void updateArtistRole(User user);

    List<RoleDTO> findAll();

    RoleDTO get(Long id);

    Long create(RoleDTO roleDTO);

    void update(Long id, RoleDTO roleDTO);

    void delete(Long id);

}
