package org.example.musicplayer.service.pleyer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.domain.entity.UserSettings;
import org.example.musicplayer.domain.repository.UserRepository;
import org.example.musicplayer.domain.repository.UserSettingsRepository;
import org.example.musicplayer.service.pleyer.UserSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;
    
    @Override
    public UserSettings save(UserSettings userSettings) {
        log.info("Saving user settings for user id: {}", userSettings.getUser().getId());
        return userSettingsRepository.save(userSettings);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserSettings> findAll() {
        log.info("Fetching all user settings");
        return userSettingsRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserSettings> findById(Long id) {
        log.info("Fetching user settings with id: {}", id);
        return userSettingsRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserSettings> findByUserId(Long userId) {
        log.info("Fetching user settings for user with id: {}", userId);
        return userSettingsRepository.findByUserId(userId);
    }
    
    @Override
    public UserSettings createDefaultSettings(Long userId) {
        log.info("Creating default settings for user with id: {}", userId);
        
        Optional<UserSettings> existingSettings = userSettingsRepository.findByUserId(userId);
        if (existingSettings.isPresent()) {
            log.info("User settings already exist for user id: {}", userId);
            return existingSettings.get();
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        UserSettings userSettings = UserSettings.builder()
                .user(user)
                .preferredQuality("HIGH")
                .language("en")
                .autoPlay(true)
                .build();
        
        return userSettingsRepository.save(userSettings);
    }
    
    @Override
    public void deleteById(Long id) {
        log.info("Deleting user settings with id: {}", id);
        userSettingsRepository.deleteById(id);
    }
    
    @Override
    public void deleteByUserId(Long userId) {
        log.info("Deleting user settings for user with id: {}", userId);
        userSettingsRepository.deleteByUserId(userId);
    }
} 