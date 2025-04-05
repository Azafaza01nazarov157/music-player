package org.example.musicplayer.service.pleyer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Artist;
import org.example.musicplayer.domain.repository.ArtistRepository;
import org.example.musicplayer.dtos.artist.ArtistDTO;
import org.example.musicplayer.exception.dto.ErrorDto;
import org.example.musicplayer.exception.errors.NotFoundException;
import org.example.musicplayer.mapper.ArtistMapper;
import org.example.musicplayer.service.pleyer.ArtistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public ArtistDTO save(ArtistDTO artistDTO) {
        log.info("Saving artist: {}", artistDTO.getName());
        Artist artist = artistMapper.toEntity(artistDTO);
        artist = artistRepository.save(artist);
        return artistMapper.toDto(artist);
    }

    @Override
    public ArtistDTO update(Long id, ArtistDTO artistDTO) {
        log.info("Updating artist with id: {}", id);
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorDto("404", "Artist not found with id: " + id)));

        artist = artistMapper.updateEntity(artistDTO, artist);
        artist = artistRepository.save(artist);
        return artistMapper.toDto(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistDTO> findAll() {
        log.info("Fetching all artists");
        return artistRepository.findAll().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArtistDTO> findById(Long id) {
        log.info("Fetching artist with id: {}", id);
        return artistRepository.findById(id)
                .map(artistMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistDTO> findByName(String name) {
        log.info("Fetching artists with name containing: {}", name);
        return artistRepository.findByNameContainingIgnoreCase(name).stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting artist with id: {}", id);
        artistRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return artistRepository.existsByName(name);
    }
}