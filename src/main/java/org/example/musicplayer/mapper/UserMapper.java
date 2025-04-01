package org.example.musicplayer.mapper;

import org.example.musicplayer.domain.entity.Role;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.dtos.user.UserDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDTO updateUserDTO(User user, @MappingTarget UserDTO userDTO);

    @AfterMapping
    default void afterUpdateUserDTO(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setRoles(user.getRoles().stream()
                .map(Role::getId)
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    User updateUser(UserDTO userDTO, @MappingTarget User user,
                    @Context RoleRepository roleRepository, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void afterUpdateUser(UserDTO userDTO, @MappingTarget User user,
                                 @Context RoleRepository roleRepository, @Context PasswordEncoder passwordEncoder) {
        final List<Role> roles = roleRepository.findAllById(
                userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new NotFoundException(new ErrorDto("404", "Not Found one of roles not found"));
        }
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

}
