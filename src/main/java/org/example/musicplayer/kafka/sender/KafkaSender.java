package org.example.musicplayer.kafka.sender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@RequiredArgsConstructor
public class KafkaSender {
    private final KafkaTemplate<String, Object> template;

    public <T> void sendAuth(String topic, @NotNull @NotBlank String key, @NotNull T value) {
        template.send(topic, key, value);
    }

    public <T> void send(String topic, @NotNull @NotBlank String key, Integer partition, @NotNull T value) {
        template.send(topic, partition, key, value);
    }
}
