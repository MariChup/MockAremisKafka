package org.example.Consumers;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.List;
import java.util.Properties;

public class KafkaMessageConsumer {
    private final KafkaConsumer<String, String> consumer;

    public KafkaMessageConsumer(Properties kafkaProperties, String topic) {
        kafkaProperties.put("key.deserializer", StringDeserializer.class.getName());
        kafkaProperties.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(kafkaProperties);
        this.consumer.subscribe(List.of(topic));
    }

    public void consumeMessages() {
        while (true) {
            var records = consumer.poll(1000);
            records.forEach(record -> {
                System.out.println("Consumed message from topic [" + record.topic() + "]: " + record.value());
            });
        }
    }
}
