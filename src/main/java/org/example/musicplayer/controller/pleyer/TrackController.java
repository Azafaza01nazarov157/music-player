package org.example.musicplayer.controller.pleyer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.track.CreateTrackDTO;
import org.example.musicplayer.dtos.track.TrackDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.ForbiddenException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.model.TrackProcessingDto;
import org.example.musicplayer.service.TrackUploadService;
import org.example.musicplayer.service.pleyer.TrackService;
import org.example.musicplayer.service.user.UserService;
import org.example.musicplayer.util.UserRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

import static org.example.musicplayer.config.constraint.Endpoints.API;

@Slf4j
@RestController
@RequestMapping(API + "/tracks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class TrackController {

    private final TrackService trackService;
    private final TrackUploadService trackUploadService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ARTIST')")
    public ResponseEntity<CreateTrackDTO> createTrack(@RequestBody CreateTrackDTO trackDTO) {
        log.info("REST request to create Track: {}", trackDTO.getTitle());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());
        
        boolean isArtist = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ARTIST"));
        
        if (!isArtist) {
            throw new ForbiddenException(new ErrorDto("403", "Only artists can create tracks"));
        }


        CreateTrackDTO result = trackService.save(trackDTO,currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ARTIST')")
    public ResponseEntity<TrackDTO> updateTrack(
            @PathVariable Long id,
            @RequestBody TrackDTO trackDTO) {
        log.info("REST request to update Track: {}, {}", id, trackDTO.getTitle());
        
        TrackDTO existingTrack = trackService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Track not found with id: " + id)));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());
        
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        
        if (!isAdmin && !existingTrack.getUserId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    new ErrorDto("403", "You don't have permission to edit this track"));
        }
        
        TrackDTO result = trackService.update(id, trackDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<TrackDTO> updateTrackStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("REST request to update Track status: {}, {}", id, status);
        TrackDTO result = trackService.updateTrackStatus(id, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks() {
        log.info("REST request to get all Tracks");
        List<TrackDTO> tracks = trackService.findAll();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable Long id) {
        log.info("REST request to get Track: {}", id);
        TrackDTO trackDTO = trackService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Track not found with id: " + id)));
        return ResponseEntity.ok(trackDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrackDTO>> getTracksByUser(@PathVariable Long userId) {
        log.info("REST request to get Tracks by User: {}", userId);
        List<TrackDTO> tracks = trackService.findByUserId(userId);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<TrackDTO>> getTracksByAlbum(@PathVariable Long albumId) {
        log.info("REST request to get Tracks by Album: {}", albumId);
        List<TrackDTO> tracks = trackService.findByAlbumId(albumId);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<List<TrackDTO>> getTracksByStatus(@PathVariable String status) {
        log.info("REST request to get Tracks by Status: {}", status);
        List<TrackDTO> tracks = trackService.findByStatus(status);
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/{id}/increment-play")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<Void> incrementPlayCount(@PathVariable Long id) {
        log.info("REST request to increment play count for Track: {}", id);
        trackService.incrementPlayCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        log.info("REST request to delete Track: {}", id);
        
        TrackDTO existingTrack = trackService.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Track not found with id: " + id)));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());
        
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        
        if (!isAdmin && !existingTrack.getUserId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    new ErrorDto("403", "You don't have permission to delete this track"));
        }
        
        trackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.ARTIST + "')")
    public ResponseEntity<TrackProcessingDto> uploadTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("trackId") String trackId,
            Principal principal
    ) {

        User user = userService.getUserByEmail(principal.getName());
        TrackProcessingDto request = trackUploadService.uploadAndProcessTrack(file, trackId, user.getId().toString());

        return ResponseEntity.ok(request);
    }
}