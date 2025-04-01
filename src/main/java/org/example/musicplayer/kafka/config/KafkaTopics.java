package org.example.musicplayer.kafka.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.kafka.topics")
public class KafkaTopics {

    private String audioProcessing;
}