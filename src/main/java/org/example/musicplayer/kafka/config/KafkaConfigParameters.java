package org.example.musicplayer.kafka.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Set;

@Builder
public record KafkaConfigParameters(@NotEmpty Set<String> trustedPackages, @NotEmpty Set<String> topicNames) {
}
