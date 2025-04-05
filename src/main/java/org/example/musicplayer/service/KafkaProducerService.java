package org.example.musicplayer.service;

import org.example.musicplayer.kafka.sender.KafkaSender;
import org.example.musicplayer.model.TrackProcessingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final ObjectMapper objectMapper;
    private final KafkaSender kafkaSender;

    @Value("${spring.kafka.topics.audio-processing}")
    private String audioProcessingTopic;

    /**
     * Sends a track processing request to Kafka
     * 
     * @param request The track processing request
     * @return CompletableFuture<SendResult> with the result of the send operation
     */
//    public CompletableFuture<SendResult<String, String>> sendTrackProcessingRequest(TrackProcessingRequest request) {
//        try {
//            String key = request.getTrackId();
//            String value = objectMapper.writeValueAsString(request);
//
//            log.info("Sending track processing request to Kafka: {}", key);
//
//            CompletableFuture<SendResult<String, String>> future = kafkaSender.sendAuthCompletableFuture(audioProcessingTopic, key, value);
//
//            future.whenComplete((result, ex) -> {
//                if (ex == null) {
//                    log.info("Track processing request sent successfully: {} - offset: {}",
//                            key, result.getRecordMetadata().offset());
//                } else {
//                    log.error("Unable to send track processing request: {}", ex.getMessage(), ex);
//                }
//            });
//
//            return future;
//
//        } catch (Exception e) {
//            log.error("Error serializing or sending track processing request: {}", e.getMessage(), e);
//            throw new RuntimeException("Could not send track processing request", e);
//        }
//    }
} 