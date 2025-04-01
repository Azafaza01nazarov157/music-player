package org.example.musicplayer.kafka.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.musicplayer.kafka.config.KafkaProps;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class KafkaSupport {

    public static void validateBootstrapServices(KafkaProps.KafkaBootstrapServersProps kafkaProps) {
        if (StringUtils.isBlank(kafkaProps.getBootstrapServers())) {
            throw new IllegalArgumentException("Empty kafka bootstrapServers. Please check configuration.");
        }
    }

    public static void createTopicsIfNeeded(Collection<NewTopic> topics, KafkaProps.KafkaConsumerProps props) {
        validateBootstrapServices(props);
        if (!CollectionUtils.isEmpty(topics)) {
            try (AdminClient client = AdminClient.create(Map.of(
                    AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers()))) {
                client.createTopics(topics, new CreateTopicsOptions().validateOnly(false));
            }
        }
    }

    public static <T> ConcurrentKafkaListenerContainerFactory<String, String> createManualAckKafkaConsumerFactory(
            KafkaProps.KafkaConsumerProps kafkaProps, Set<String> trustedPackages) {
        validateBootstrapServices(kafkaProps);

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        KafkaJsonDeserializer<T> jsonDeserializer = new KafkaJsonDeserializer<>();
        if (!CollectionUtils.isEmpty(trustedPackages)) {
            jsonDeserializer.configure(Map.of(
                    JsonDeserializer.TRUSTED_PACKAGES, String.join(",", trustedPackages)), false);
        }

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProps.getGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false
        ), new StringDeserializer(), new StringDeserializer()));
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(kafkaProps.getBackoffInterval(), kafkaProps.getMaxAttempts())));
        return factory;
    }

    public static <T> ProducerFactory<String, T> createKafkaProducerFactory(KafkaProps.KafkaProducerProps kafkaProps) {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class,
                ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaProps.getMaxRequestSize(),
                ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProps.getBufferMemory()
        ), new StringSerializer(), new KafkaJsonSerializer<>());
    }
}
