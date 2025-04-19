package org.example.musicplayer.controller.pleyer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.repository.RoleRepository;
import org.example.musicplayer.dtos.album.AlbumDTO;
import org.example.musicplayer.dtos.album.CreateAlbumDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.ForbiddenException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.pleyer.AlbumService;
import org.example.musicplayer.service.user.UserService;
import org.example.musicplayer.util.UserRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/albums")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class AlbumController {

    private final AlbumService albumService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<CreateAlbumDTO> createAlbum(@RequestBody CreateAlbumDTO albumDTO) {
        log.info("REST request to create Album: {}", albumDTO.getTitle());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

        CreateAlbumDTO result = albumService.save(albumDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        log.info("REST request to get all Albums");
        List<AlbumDTO> albums = albumService.findAll();
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable Long id) {
        log.info("REST request to get Album: {}", id);
        AlbumDTO albumDTO = albumService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Album not found with id: " + id)));
        return ResponseEntity.ok(albumDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByUser(@PathVariable Long userId) {
        log.info("REST request to get Albums by User: {}", userId);
        List<AlbumDTO> albums = albumService.findByUserId(userId);
        return ResponseEntity.ok(albums);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<AlbumDTO> updateAlbum(
            @PathVariable Long id,
            @RequestBody AlbumDTO albumDTO) {
        log.info("REST request to update Album: {}, {}", id, albumDTO.getTitle());

        // Проверка существования альбома
        AlbumDTO existingAlbum = albumService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Album not found with id: " + id)));

        // Проверка прав на редактирование
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        // Только владелец альбома или админ может редактировать
        if (!isAdmin && !existingAlbum.getUserId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    new ErrorDto("403", "You don't have permission to edit this album"));
        }

        AlbumDTO result = albumService.update(id, albumDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.info("REST request to delete Album: {}", id);

        // Проверка существования альбома
        AlbumDTO existingAlbum = albumService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Album not found with id: " + id)));

        // Проверка прав на удаление
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        // Только владелец альбома или админ может удалять
        if (!isAdmin && !existingAlbum.getUserId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    new ErrorDto("403", "You don't have permission to delete this album"));
        }

        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}