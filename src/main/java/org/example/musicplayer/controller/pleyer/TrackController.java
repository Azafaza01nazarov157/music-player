package org.example.musicplayer.controller.pleyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.dtos.track.TrackDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.model.TrackProcessingRequest;
import org.example.musicplayer.service.TrackUploadService;
import org.example.musicplayer.service.pleyer.TrackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final TrackUploadService trackUploadService;

    @PostMapping
    public ResponseEntity<TrackDTO> createTrack(@RequestBody TrackDTO trackDTO) {
        log.info("REST request to create Track: {}", trackDTO.getTitle());
        TrackDTO result = trackService.save(trackDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> updateTrack(
            @PathVariable Long id,
            @RequestBody TrackDTO trackDTO) {
        log.info("REST request to update Track: {}, {}", id, trackDTO.getTitle());
        TrackDTO result = trackService.update(id, trackDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TrackDTO> updateTrackStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("REST request to update Track status: {}, {}", id, status);
        TrackDTO result = trackService.updateTrackStatus(id, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) String status) {
        
        log.info("REST request to get Tracks with filters: title={}, artistId={}, albumId={}, status={}",
                title, artistId, albumId, status);
        
        List<TrackDTO> result;
        
        if (title != null && !title.isEmpty()) {
            result = trackService.findByTitle(title);
        } else if (artistId != null) {
            result = trackService.findByArtistId(artistId);
        } else if (albumId != null) {
            result = trackService.findByAlbumId(albumId);
        } else if (status != null && !status.isEmpty()) {
            result = trackService.findByStatus(status);
        } else {
            result = trackService.findAll();
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable Long id) {
        log.info("REST request to get Track: {}", id);
        return trackService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404","Track not found with id: " + id)));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<TrackDTO>> getTracksByArtist(@PathVariable Long artistId) {
        log.info("REST request to get Tracks by Artist: {}", artistId);
        List<TrackDTO> tracks = trackService.findByArtistId(artistId);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<TrackDTO>> getTracksByAlbum(@PathVariable Long albumId) {
        log.info("REST request to get Tracks by Album: {}", albumId);
        List<TrackDTO> tracks = trackService.findByAlbumId(albumId);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrackDTO>> getTracksByStatus(@PathVariable String status) {
        log.info("REST request to get Tracks by Status: {}", status);
        List<TrackDTO> tracks = trackService.findByStatus(status);
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/{id}/increment-play")
    public ResponseEntity<Void> incrementPlayCount(@PathVariable Long id) {
        log.info("REST request to increment play count for Track: {}", id);
        trackService.incrementPlayCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        log.info("REST request to delete Track: {}", id);
        trackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackProcessingRequest> uploadTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("trackId") String trackId,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "duration", required = false) Integer duration,
            @RequestParam(value = "releaseDate", required = false) String releaseDate,
            @RequestParam(value = "isPublic", defaultValue = "true") boolean isPublic,
            Authentication authentication
    ) {
        log.info("Received track upload request: {}, artist: {}", title, artist);

        String userId = authentication.getName();

        TrackProcessingRequest request = trackUploadService.uploadAndProcessTrack(
                file, trackId, userId, title, artist, album, genre, duration, releaseDate, isPublic
        );

        return ResponseEntity.ok(request);
    }
}