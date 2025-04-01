package org.example.musicplayer.kafka.support;

import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * Generic {@link org.apache.kafka.common.serialization.Deserializer Deserializer} for
 * receiving JSON from Kafka and return Java objects.
 *
 * @param <T> class of the entity, representing messages
 */
public class KafkaJsonDeserializer<T> extends JsonDeserializer<T> {
    public KafkaJsonDeserializer() {
        super(KafkaJsonMapper.get());
    }
}
