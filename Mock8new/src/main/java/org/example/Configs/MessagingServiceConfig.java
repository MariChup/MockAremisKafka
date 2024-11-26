package org.example.Configs;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.MessagingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MessagingServiceConfig {

    @Bean
    public MessagingService messagingService() {
        // Конфигурация для Artemis (не меняется)
        String artemisBrokerURL = "your-activemq-broker-url";
        String artemisUsername = "your-username";
        String artemisPassword = "your-password";

        // Конфигурация для Kafka
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");  // Пример
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put("group.id", "example-group");
        kafkaProperties.put("auto.offset.reset", "earliest");

        // Создаём и возвращаем MessagingService
        String kafkaTopic = "your-kafka-topic-name";  // Укажите свой топик

        return new MessagingService(artemisBrokerURL, artemisUsername, artemisPassword, kafkaProperties, kafkaTopic);
    }
}
