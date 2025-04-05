package org.example.musicplayer.service.pleyer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.repository.AlbumRepository;
import org.example.musicplayer.dtos.album.AlbumDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.AlbumMapper;
import org.example.musicplayer.service.pleyer.AlbumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public AlbumDTO save(AlbumDTO albumDTO) {
        log.info("Saving album: {}", albumDTO.getTitle());
        Album album = albumMapper.toEntity(albumDTO);
        album = albumRepository.save(album);
        return albumMapper.toDto(album);
    }

    @Override
    public AlbumDTO update(Long id, AlbumDTO albumDTO) {
        log.info("Updating album with id: {}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Album not found with id: " + id)));

        album = albumMapper.updateEntity(albumDTO, album);
        album = albumRepository.save(album);
        return albumMapper.toDto(album);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> findAll() {
        log.info("Fetching all albums");
        return albumRepository.findAll().stream()
                .map(albumMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlbumDTO> findById(Long id) {
        log.info("Fetching album with id: {}", id);
        return albumRepository.findById(id)
                .map(albumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> findByArtistId(Long artistId) {
        log.info("Fetching albums for artist with id: {}", artistId);
        return albumRepository.findByArtistId(artistId).stream()
                .map(albumMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> findByTitle(String title) {
        log.info("Fetching albums with title containing: {}", title);
        return albumRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(albumMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> findByArtistIdAndTitle(Long artistId, String title) {
        log.info("Fetching albums for artist id: {} with title containing: {}", artistId, title);
        return albumRepository.findByArtistIdAndTitleContainingIgnoreCase(artistId, title).stream()
                .map(albumMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting album with id: {}", id);
        albumRepository.deleteById(id);
    }
}