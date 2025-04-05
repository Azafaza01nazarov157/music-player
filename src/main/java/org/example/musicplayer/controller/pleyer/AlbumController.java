package org.example.musicplayer.controller.pleyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.dtos.album.AlbumDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.pleyer.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody AlbumDTO albumDTO) {
        log.info("REST request to create Album: {}", albumDTO);
        AlbumDTO result = albumService.save(albumDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(
            @PathVariable Long id,
            @RequestBody AlbumDTO albumDTO) {
        log.info("REST request to update Album: {}, {}", id, albumDTO);
        AlbumDTO result = albumService.update(id, albumDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) String title) {
        log.info("REST request to get all Albums with filters: artistId={}, title={}", artistId, title);

        List<AlbumDTO> result;
        if (artistId != null && title != null) {
            result = albumService.findByArtistIdAndTitle(artistId, title);
        } else if (artistId != null) {
            result = albumService.findByArtistId(artistId);
        } else if (title != null) {
            result = albumService.findByTitle(title);
        } else {
            result = albumService.findAll();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable Long id) {
        log.info("REST request to get Album: {}", id);
        return albumService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Album not found with id: {}" + id)));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByArtist(@PathVariable Long artistId) {
        log.info("REST request to get Albums by Artist: {}", artistId);
        List<AlbumDTO> albums = albumService.findByArtistId(artistId);
        return ResponseEntity.ok(albums);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.info("REST request to delete Album: {}", id);
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}