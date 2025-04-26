package org.example.musicplayer.service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.domain.entity.Album;
import org.example.musicplayer.domain.entity.Track;
import org.example.musicplayer.domain.entity.User;
import org.example.musicplayer.dtos.kafka.AlbumMessage;
import org.example.musicplayer.dtos.kafka.TrackMessage;
import org.example.musicplayer.dtos.kafka.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaIntegrationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestTemplate restTemplate;

    @Value("${spring.kafka.topics.user-sync}")
    private String userSyncTopic;

    @Value("${spring.kafka.topics.track-sync}")
    private String trackSyncTopic;

    @Value("${spring.kafka.topics.album-sync}")
    private String albumSyncTopic;

    /**
     * Отправляет информацию о пользователе в Kafka для синхронизации с Go-сервисом
     * @param user Объект пользователя
     */
    public void sendUserToKafka(User user) {
        try {
            UserMessage userMessage = UserMessage.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(getUserRole(user))
                    .createdAt(String.valueOf(user.getCreatedDate()))
                    .updatedAt(String.valueOf(user.getLastModifiedDate()))
                    .isDeleted(false)
                    .build();

            log.info("Sending user to REST API for sync: {}", user.getUsername());

            String apiUrl = "http://localhost:8080/api/users/sine";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserMessage> request = new HttpEntity<>(userMessage, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User successfully sent to REST API: {}", user.getUsername());
            } else {
                log.error("Failed to send user to REST API. Status code: {}", response.getStatusCodeValue());
            }

        } catch (Exception e) {
            log.error("Error sending user to REST API: {}", e.getMessage(), e);
        }
    }

    /**
     * Отправляет информацию о треке в Kafka для синхронизации с Go-сервисом
     * @param track Объект трека
     */
    public void sendTrackToKafka(Track track) {
        try {
            Long albumId = track.getAlbum() != null ? track.getAlbum().getId() : null;
            
            TrackMessage trackMessage = TrackMessage.builder()
                    .id(track.getId())
                    .title(track.getTitle())
                    .artistId(track.getUser().getId())
                    .albumId(albumId)
                    .userId(track.getUser().getId())
                    .filePath(track.getFilePath())
                    .fileSize(track.getFileSize())
                    .fileFormat(track.getFileFormat())
                    .duration(track.getDuration() != null ? track.getDuration().doubleValue() : null)
                    .bitRate(track.getBitRate())
                    .sampleRate(track.getSampleRate())
                    .trackNumber(track.getTrack_number())
                    .genre(track.getGenre())
                    .playCount(track.getPlayCount())
                    .createdAt(String.valueOf(track.getCreatedDate()))
                    .updatedAt(String.valueOf(track.getLastModifiedDate()))
                    .isDeleted(false)
                    .build();

            log.info("Sending track to Kafka for sync: {}", track.getTitle());
            kafkaTemplate.send(trackSyncTopic, track.getId().toString(), trackMessage);
        } catch (Exception e) {
            log.error("Error sending track to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Отправляет информацию об альбоме в Kafka для синхронизации с Go-сервисом
     * @param album Объект альбома
     */
    public void sendAlbumToKafka(Album album) {
        try {
            AlbumMessage albumMessage = AlbumMessage.builder()
                    .id(album.getId())
                    .title(album.getTitle())
                    .userId(album.getUser().getId())
                    .releaseDate(album.getReleaseDate() != null ? album.getReleaseDate().toString() : null)
                    .coverUrl(album.getCoverUrl())
                    .genre(album.getGenre())
                    .description(album.getDescription())
                    .createdAt(String.valueOf(album.getCreatedDate()))
                    .updatedAt(String.valueOf(album.getLastModifiedDate()))
                    .isDeleted(false)
                    .build();

            log.info("Sending album to Kafka for sync: {}", album.getTitle());
            kafkaTemplate.send(albumSyncTopic, album.getId().toString(), albumMessage);
        } catch (Exception e) {
            log.error("Error sending album to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Отправляет информацию об удаленном пользователе в Kafka
     * @param userId ID пользователя
     */
    public void sendUserDeletedToKafka(Long userId) {
        try {
            UserMessage userMessage = UserMessage.builder()
                    .id(userId)
                    .isDeleted(true)
                    .updatedAt(String.valueOf(OffsetDateTime.now()))
                    .build();

            log.info("Sending user deleted event to Kafka for user id: {}", userId);
            kafkaTemplate.send(userSyncTopic, userId.toString(), userMessage);
        } catch (Exception e) {
            log.error("Error sending user deleted event to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Отправляет информацию об удаленном треке в Kafka
     * @param trackId ID трека
     */
    public void sendTrackDeletedToKafka(Long trackId) {
        try {
            TrackMessage trackMessage = TrackMessage.builder()
                    .id(trackId)
                    .isDeleted(true)
                    .updatedAt(String.valueOf(OffsetDateTime.now()))
                    .build();

            log.info("Sending track deleted event to Kafka for track id: {}", trackId);
            kafkaTemplate.send(trackSyncTopic, trackId.toString(), trackMessage);
        } catch (Exception e) {
            log.error("Error sending track deleted event to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Отправляет информацию об удаленном альбоме в Kafka
     * @param albumId ID альбома
     */
    public void sendAlbumDeletedToKafka(Long albumId) {
        try {
            AlbumMessage albumMessage = AlbumMessage.builder()
                    .id(albumId)
                    .isDeleted(true)
                    .updatedAt(String.valueOf(OffsetDateTime.now()))
                    .build();

            log.info("Sending album deleted event to Kafka for album id: {}", albumId);
            kafkaTemplate.send(albumSyncTopic, albumId.toString(), albumMessage);
        } catch (Exception e) {
            log.error("Error sending album deleted event to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Получает строковое представление роли пользователя для Go-сервиса
     * @param user Объект пользователя
     * @return Роль в строковом формате
     */
    private String getUserRole(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return "user";
        }
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
        
        if (isAdmin) {
            return "admin";
        }
        
        boolean isArtist = user.getRoles().stream()
                .anyMatch(role -> "ARTIST".equalsIgnoreCase(role.getName()));
        
        if (isArtist) {
            return "artist";
        }
        
        return "user";
    }
}