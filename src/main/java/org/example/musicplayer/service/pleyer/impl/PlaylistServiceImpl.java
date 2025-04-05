package org.example.musicplayer.service.pleyer.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Playlist;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.repository.PlaylistRepository;
import org.example.musicplayer.domain.repository.TrackRepository;
import org.example.musicplayer.dtos.playlist.PlaylistDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.BadRequestException;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.PlaylistMapper;
import org.example.musicplayer.service.pleyer.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final PlaylistMapper playlistMapper;

    @Override
    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        log.info("Saving playlist: {}", playlistDTO.getName());

        Playlist playlist = playlistMapper.toEntity(playlistDTO);
        playlist = playlistRepository.save(playlist);

        return playlistMapper.toDTO(playlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findAll() {
        log.info("Fetching all playlists");
        List<Playlist> playlists = playlistRepository.findAll();
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlaylistDTO> findById(Long id) {
        log.info("Fetching playlist with id: {}", id);
        return playlistRepository.findById(id)
                .map(playlistMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findByUserId(Long userId) {
        log.info("Fetching playlists for user with id: {}", userId);
        List<Playlist> playlists = playlistRepository.findByUserId(userId);
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findByName(String name) {
        log.info("Fetching playlists with name containing: {}", name);
        List<Playlist> playlists = playlistRepository.findByNameContainingIgnoreCase(name);
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findByUserIdAndName(Long userId, String name) {
        log.info("Fetching playlists for user id: {} with name containing: {}", userId, name);
        List<Playlist> playlists = playlistRepository.findByUserIdAndNameContainingIgnoreCase(userId, name);
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findAllPublic() {
        log.info("Fetching all public playlists");
        List<Playlist> playlists = playlistRepository.findAll();
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findPublicByName(String name) {
        log.info("Fetching public playlists with name containing: {}", name);
        List<Playlist> playlists = playlistRepository.findPublicByNameContainingIgnoreCase(name);
        return playlistMapper.toDTOList(playlists);
    }

    @Override
    public PlaylistDTO addTrackToPlaylist(Long playlistId, Long trackId) {
        log.info("Adding track id: {} to playlist id: {}", trackId, playlistId);

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Playlist not found with id: " + playlistId)));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Track not found with id: " + trackId)));

        if (playlist.getTracks() == null) {
            playlist.setTracks(new HashSet<>());
        }

        playlist.getTracks().add(track);
        playlist = playlistRepository.save(playlist);

        return playlistMapper.toDTO(playlist);
    }

    @Override
    public PlaylistDTO removeTrackFromPlaylist(Long playlistId, Long trackId) {
        log.info("Removing track id: {} from playlist id: {}", trackId, playlistId);

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Playlist not found with id: " + playlistId)));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Track not found with id: " + trackId)));

        if (playlist.getTracks() != null) {
            playlist.getTracks().remove(track);
            playlist = playlistRepository.save(playlist);
        }

        return playlistMapper.toDTO(playlist);
    }

    @Override
    public PlaylistDTO update(Long id, PlaylistDTO playlistDTO) {
        log.info("Updating playlist with id: {}", id);

        Playlist existingPlaylist = playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ErrorDto("404", "Playlist not found with id: " + id)));

        if (playlistDTO.getId() != null && !playlistDTO.getId().equals(id)) {
            throw new BadRequestException(new ErrorDto("400", "ID in the request body does not match the path parameter"));
        }

        playlistDTO.setId(id);

        Playlist updatedPlaylist = playlistMapper.updatePlaylistFromDTO(playlistDTO, existingPlaylist);
        updatedPlaylist = playlistRepository.save(updatedPlaylist);

        return playlistMapper.toDTO(updatedPlaylist);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting playlist with id: {}", id);
        if (!playlistRepository.existsById(id)) {
            throw new NotFoundException(new ErrorDto("404", "Playlist not found with id: " + id));
        }
        playlistRepository.deleteById(id);
    }
}