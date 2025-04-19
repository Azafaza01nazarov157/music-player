package org.example.musicplayer.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Role;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.dtos.user.UserDTO;
import org.example.musicplayer.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllArtists() {
        log.info("REST request to get all Artists");
        
        Set<Role> artistRoles = roleRepository.findByName("ARTIST");
        if (artistRoles.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        
        List<User> artists = userRepository.findAllByRoles(artistRoles.iterator().next());
        
        List<UserDTO> artistDTOs = artists.stream()
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(artistDTOs);
    }
} 