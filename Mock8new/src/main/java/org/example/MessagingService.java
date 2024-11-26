package org.example;

import org.example.Configs.ArtemisConnectionManager;
import org.example.Consumers.ArtemisMessageConsumer;
import org.example.Consumers.KafkaMessageConsumer;
import org.example.Producers.ArtemisMessageSender;
import org.example.Producers.KafkaMessageSender;
import org.springframework.stereotype.Service;

import javax.jms.Connection;
import java.util.Properties;

public class MessagingService { // Убираем @Service

    private final ArtemisMessageSender artemisSender;
    private final KafkaMessageSender kafkaSender;
    private final KafkaMessageConsumer kafkaConsumer;
    private final String kafkaTopic;

    public MessagingService(String artemisBrokerURL, String artemisUsername, String artemisPassword,
                            Properties kafkaProperties, String kafkaTopic) {
        this.artemisSender = new ArtemisMessageSender(artemisBrokerURL, artemisUsername, artemisPassword);
        this.kafkaSender = new KafkaMessageSender(kafkaProperties);
        this.kafkaTopic = kafkaTopic;

        // Создаем консюмера Kafka
        this.kafkaConsumer = new KafkaMessageConsumer(kafkaProperties, kafkaTopic);
    }

    public void sendMessage(String artemisQueue, String kafkaTopic, String message) {
        try {
            artemisSender.sendMessageToQueue(artemisQueue, message);
            kafkaSender.sendMessageToTopic(kafkaTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startConsuming(String artemisQueue) {
        try {
            // Запуск Artemis Consumer в отдельном потоке
            new Thread(() -> {
                try {
                    Connection artemisConnection = ArtemisConnectionManager.getConnection(
                            artemisSender.getBrokerURL(),
                            artemisSender.getUsername(),
                            artemisSender.getPassword()
                    );

                    ArtemisMessageConsumer artemisConsumer = new ArtemisMessageConsumer(artemisConnection, artemisQueue);
                    artemisConsumer.consumeMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Запуск Kafka Consumer в отдельном потоке
            new Thread(() -> {
                try {
                    kafkaConsumer.consumeMessages(); // Запуск консюмера Kafka
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
