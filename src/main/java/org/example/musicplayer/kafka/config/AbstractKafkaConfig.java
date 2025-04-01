package org.example.musicplayer.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.musicplayer.kafka.support.KafkaSupport;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@EnableKafka
abstract class AbstractKafkaConfig {

    protected void createTopics(KafkaProps kafkaProps, Set<String> topicNames) {
        if (kafkaProps.getConsumer() != null) {
            final List<NewTopic> newTopics = topicNames.stream()
                    .map(topicName -> new NewTopic(topicName,
                            Optional.of(kafkaProps.getDefaultTopicPartitions()),
                            Optional.of(kafkaProps.getDefaultReplicationFactor())))
                    .collect(Collectors.toList());

            KafkaSupport.createTopicsIfNeeded(newTopics, kafkaProps.getConsumer());
        }
    }
}
