package org.example.musicplayer.service.user;

import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.user.UserDTO;
import org.example.musicplayer.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    Page<UserDTO> findAll(String filter, Pageable pageable);

    UserDTO get(String email);

    Long create(UserDTO userDTO);

    void update(Long id, UserDTO userDTO);

    void delete(Long id);

    boolean emailExists(String email);

    ReferencedWarning getReferencedWarning(Long id);

    void changePassword(Long userId, String oldPassword, String newPassword);

    User getUserByEmail(String email);

    User getUserById(final Long id);
}
