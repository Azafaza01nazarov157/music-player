package org.example.musicplayer.controller.pleyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.dtos.artist.ArtistDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.service.pleyer.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) {
        log.info("REST request to create Artist: {}", artistDTO.getName());

        if (artistService.existsByName(artistDTO.getName())) {
            log.warn("Artist with name '{}' already exists", artistDTO.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ArtistDTO result = artistService.save(artistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(
            @PathVariable Long id,
            @RequestBody ArtistDTO artistDTO) {
        log.info("REST request to update Artist: {}, {}", id, artistDTO.getName());

        if (artistService.findById(id).isEmpty()) {
            log.warn("Artist with id '{}' not found", id);
            throw new NotFoundException(new ErrorDto("404", "Artist not found with id: " + id));
        }

        ArtistDTO result = artistService.update(id, artistDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists(
            @RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            log.info("REST request to find Artists by name: {}", name);
            List<ArtistDTO> artists = artistService.findByName(name);
            return ResponseEntity.ok(artists);
        } else {
            log.info("REST request to get all Artists");
            List<ArtistDTO> artists = artistService.findAll();
            return ResponseEntity.ok(artists);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable Long id) {
        log.info("REST request to get Artist: {}", id);
        return artistService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Artist not found with id: " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        log.info("REST request to delete Artist: {}", id);

        if (artistService.findById(id).isEmpty()) {
            log.warn("Artist with id '{}' not found", id);
            throw new NotFoundException(new ErrorDto("404", "Artist not found with id: " + id));
        }

        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> artistExistsByName(@PathVariable String name) {
        log.info("REST request to check if Artist exists by name: {}", name);
        boolean exists = artistService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}