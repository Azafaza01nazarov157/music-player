package org.example.musicplayer.kafka.support;

import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Generic {@link org.apache.kafka.common.serialization.Serializer Serializer} for sending
 * Java objects to Kafka as JSON.
 *
 * @param <T> class of the entity, representing messages
 */
public class KafkaJsonSerializer<T> extends JsonSerializer<T> {
    public KafkaJsonSerializer() {
        super(KafkaJsonMapper.get());
    }
}
