package org.example.musicplayer.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.musicplayer.kafka.config.KafkaConfig.KAFKA_CONTAINER_FACTORY_BEAN;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topics.audio-processing}", containerFactory = KAFKA_CONTAINER_FACTORY_BEAN)
    public void listenAuthResponse(@NotNull ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            ObjectMapper mapper = new ObjectMapper();
        } catch (Exception e) {
            log.error("Error processing record", e);
        } finally {
            ack.acknowledge();
        }
    }
}
