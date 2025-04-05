package org.example.musicplayer.controller.pleyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.dtos.playlist.PlaylistDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.pleyer.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        log.info("REST request to create Playlist: {}", playlistDTO.getName());
        PlaylistDTO result = playlistService.save(playlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(
            @PathVariable Long id,
            @RequestBody PlaylistDTO playlistDTO) {
        log.info("REST request to update Playlist: {}, {}", id, playlistDTO.getName());
        PlaylistDTO result = playlistService.update(id, playlistDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean publicOnly) {

        log.info("REST request to get Playlists with filters: userId={}, name={}, publicOnly={}",
                userId, name, publicOnly);

        List<PlaylistDTO> result;

        if (Boolean.TRUE.equals(publicOnly)) {
            if (name != null && !name.isEmpty()) {
                result = playlistService.findPublicByName(name);
            } else {
                result = playlistService.findAllPublic();
            }
        } else if (userId != null && name != null && !name.isEmpty()) {
            result = playlistService.findByUserIdAndName(userId, name);
        } else if (userId != null) {
            result = playlistService.findByUserId(userId);
        } else if (name != null && !name.isEmpty()) {
            result = playlistService.findByName(name);
        } else {
            result = playlistService.findAll();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylist(@PathVariable Long id) {
        log.info("REST request to get Playlist: {}", id);
        return playlistService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Playlist not found with id: " + id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDTO>> getPlaylistsByUser(@PathVariable Long userId) {
        log.info("REST request to get Playlists by User: {}", userId);
        List<PlaylistDTO> playlists = playlistService.findByUserId(userId);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/public")
    public ResponseEntity<List<PlaylistDTO>> getPublicPlaylists() {
        log.info("REST request to get all public Playlists");
        List<PlaylistDTO> playlists = playlistService.findAllPublic();
        return ResponseEntity.ok(playlists);
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<PlaylistDTO> addTrackToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId) {
        log.info("REST request to add Track {} to Playlist {}", trackId, playlistId);
        PlaylistDTO result = playlistService.addTrackToPlaylist(playlistId, trackId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<PlaylistDTO> removeTrackFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId) {
        log.info("REST request to remove Track {} from Playlist {}", trackId, playlistId);
        PlaylistDTO result = playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        log.info("REST request to delete Playlist: {}", id);
        playlistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}