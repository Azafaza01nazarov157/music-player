package org.example.musicplayer.kafka.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@Valid
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProps {

    @NotNull
    private Integer defaultTopicPartitions;
    @NotNull
    private Short defaultReplicationFactor;

    private KafkaProducerProps producer;
    private KafkaConsumerProps consumer;

    @Getter
    @Setter
    public static class KafkaProducerProps implements KafkaBootstrapServersProps {
        @NotBlank
        private String bootstrapServers;
        @NotNull
        private Integer maxRequestSize;
        @NotNull
        private Integer bufferMemory;
    }

    @Getter
    @Setter
    public static class KafkaConsumerProps implements KafkaBootstrapServersProps {
        @NotBlank
        private String bootstrapServers;
        @NotBlank
        private String groupId;
        @NotNull
        private Long backoffInterval;
        @NotNull
        private Long maxAttempts;
    }

    public interface KafkaBootstrapServersProps {
        String getBootstrapServers();
    }
}
