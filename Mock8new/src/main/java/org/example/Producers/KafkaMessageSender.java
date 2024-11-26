package org.example.Producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaMessageSender {
    private final KafkaProducer<String, String> producer;

    public KafkaMessageSender(Properties kafkaProperties) {
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(kafkaProperties);
    }

    public void sendMessageToTopic(String topicName, String message) {
        producer.send(new ProducerRecord<>(topicName, message), (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to topic [" + topicName + "]: " + message);
            } else {
                exception.printStackTrace();
            }
        });
    }
}
