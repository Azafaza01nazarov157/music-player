package org.example.musicplayer.kafka.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.kafka.sender.KafkaSender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Set;

import static org.example.musicplayer.kafka.support.KafkaSupport.createKafkaProducerFactory;
import static org.example.musicplayer.kafka.support.KafkaSupport.createManualAckKafkaConsumerFactory;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig extends AbstractKafkaConfig {

    private static final String KAFKA_PROPS_PREFIX = "spring.kafka";
    private static final String KAFKA_CONFIG_PARAMS_BEAN = "KafkaConfigParams";
    private static final String KAFKA_PROPS_BEAN = "KafkaProps";
    private static final String KAFKA_TEMPLATE_BEAN = "KafkaTemplate";
    public static final String KAFKA_SENDER_BEAN = "KafkaSender";
    public static final String KAFKA_CONTAINER_FACTORY_BEAN = "KafkaContainerFactory";

    private final KafkaTopics kafkaTopics;

    @Bean(KAFKA_CONFIG_PARAMS_BEAN)
    KafkaConfigParameters kafkaConfigParameters() {

        Set<String> trustedPackages = Set.of(
                "org.example.integration.dto.*",
                "org.example.integration.domain.entity.*");
        Set<String> newMenuPostEventCreateTopic = Set.of(
                kafkaTopics.getAudioProcessing()
        );
        return new KafkaConfigParameters(
                trustedPackages,
                newMenuPostEventCreateTopic
        );
    }

    @Bean(KAFKA_PROPS_BEAN)
    @ConfigurationProperties(prefix = KAFKA_PROPS_PREFIX)
    KafkaProps KafkaProps() {
        return new KafkaProps();
    }

    @Bean(KAFKA_TEMPLATE_BEAN)
    KafkaTemplate<String, Object> KafkaTemplate(@Valid @NotNull @Qualifier(KAFKA_PROPS_BEAN) KafkaProps kafkaProps,
                                                @NotNull @Qualifier(KAFKA_CONFIG_PARAMS_BEAN) KafkaConfigParameters kafkaConfigParameters) {
        createTopics(kafkaProps, kafkaConfigParameters.topicNames());
        return new KafkaTemplate<>(createKafkaProducerFactory(kafkaProps.getProducer()));
    }

    @Bean(KAFKA_SENDER_BEAN)
    KafkaSender KafkaSender(@Qualifier(KAFKA_TEMPLATE_BEAN) KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaSender(kafkaTemplate);
    }

    @Bean(KAFKA_CONTAINER_FACTORY_BEAN)
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaContainerFactory(
            @Valid @NotNull @Qualifier(KAFKA_PROPS_BEAN) KafkaProps kafkaProps,
            @NotNull @Qualifier(KAFKA_CONFIG_PARAMS_BEAN) KafkaConfigParameters kafkaConfigParameters) {
        return createManualAckKafkaConsumerFactory(kafkaProps.getConsumer(), kafkaConfigParameters.trustedPackages());
    }
}
