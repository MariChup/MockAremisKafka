package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class KafkaConnectionManager {
    private static KafkaProducer<String, String> producer;

    public static synchronized KafkaProducer<String, String> getProducer(Properties kafkaProperties) {
        if (producer == null) {
            producer = new KafkaProducer<>(kafkaProperties);
        }
        return producer;
    }

    public static void closeProducer() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }
}
