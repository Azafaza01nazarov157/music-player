package org.example.musicplayer.service.pleyer;


import org.example.musicplayer.domain.entity.UserSettings;

import java.util.List;
import java.util.Optional;

public interface UserSettingsService {

    UserSettings save(UserSettings userSettings);

    List<UserSettings> findAll();

    Optional<UserSettings> findById(Long id);

    Optional<UserSettings> findByUserId(Long userId);

    UserSettings createDefaultSettings(Long userId);

    void deleteById(Long id);

    void deleteByUserId(Long userId);
} 