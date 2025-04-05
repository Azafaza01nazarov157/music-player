package org.example.musicplayer.controller.pleyer;

import org.example.musicplayer.domain.entity.UserSettings;
import org.example.musicplayer.service.pleyer.UserSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserSettings.
 */
@RestController
@RequestMapping("/api")
public class UserSettingsController {

    private final Logger log = LoggerFactory.getLogger(UserSettingsController.class);
    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    /**
     * POST  /user-settings : Create a new userSettings.
     *
     * @param userSettings the userSettings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSettings
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-settings")
    public ResponseEntity<UserSettings> createUserSettings(@RequestBody UserSettings userSettings) throws URISyntaxException {
        log.debug("REST request to save UserSettings : {}", userSettings);
        UserSettings result = userSettingsService.save(userSettings);
        return ResponseEntity
                .created(new URI("/api/user-settings/" + result.getId()))
                .body(result);
    }

    /**
     * POST  /user-settings/default/{userId} : Create default settings for a user.
     *
     * @param userId the ID of the user
     * @return the ResponseEntity with status 201 (Created) and with body the new userSettings
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-settings/default/{userId}")
    public ResponseEntity<UserSettings> createDefaultUserSettings(@PathVariable Long userId) throws URISyntaxException {
        log.debug("REST request to create default UserSettings for userId : {}", userId);
        UserSettings result = userSettingsService.createDefaultSettings(userId);
        return ResponseEntity
                .created(new URI("/api/user-settings/" + result.getId()))
                .body(result);
    }

    /**
     * PUT  /user-settings : Updates an existing userSettings.
     *
     * @param userSettings the userSettings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userSettings
     */
    @PutMapping("/user-settings")
    public ResponseEntity<UserSettings> updateUserSettings(@RequestBody UserSettings userSettings) {
        log.debug("REST request to update UserSettings : {}", userSettings);
        UserSettings result = userSettingsService.save(userSettings);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /user-settings : get all the userSettings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userSettings in body
     */
    @GetMapping("/user-settings")
    public List<UserSettings> getAllUserSettings() {
        log.debug("REST request to get all UserSettings");
        return userSettingsService.findAll();
    }

    /**
     * GET  /user-settings/:id : get the "id" userSettings.
     *
     * @param id the id of the userSettings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSettings, or with status 404 (Not Found)
     */
    @GetMapping("/user-settings/{id}")
    public ResponseEntity<UserSettings> getUserSettings(@PathVariable Long id) {
        log.debug("REST request to get UserSettings : {}", id);
        Optional<UserSettings> userSettings = userSettingsService.findById(id);
        return userSettings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET  /user-settings/user/{userId} : get the userSettings for a specific user.
     *
     * @param userId the id of the user whose settings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSettings, or with status 404 (Not Found)
     */
    @GetMapping("/user-settings/user/{userId}")
    public ResponseEntity<UserSettings> getUserSettingsByUserId(@PathVariable Long userId) {
        log.debug("REST request to get UserSettings for User : {}", userId);
        Optional<UserSettings> userSettings = userSettingsService.findByUserId(userId);
        return userSettings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE  /user-settings/:id : delete the "id" userSettings.
     *
     * @param id the id of the userSettings to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/user-settings/{id}")
    public ResponseEntity<Void> deleteUserSettings(@PathVariable Long id) {
        log.debug("REST request to delete UserSettings : {}", id);
        userSettingsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE  /user-settings/user/{userId} : delete the userSettings for a specific user.
     *
     * @param userId the id of the user whose settings to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/user-settings/user/{userId}")
    public ResponseEntity<Void> deleteUserSettingsByUserId(@PathVariable Long userId) {
        log.debug("REST request to delete UserSettings for User : {}", userId);
        userSettingsService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}