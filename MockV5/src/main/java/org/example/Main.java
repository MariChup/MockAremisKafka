package org.example;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            // Загружаем конфигурацию
            Properties config = ConfigLoader.loadConfig();

            // Инициализируем параметры для Artemis
            String artemisBrokerURL = config.getProperty("artemis.broker.url");
            String artemisUsername = config.getProperty("artemis.username");
            String artemisPassword = config.getProperty("artemis.password");

            // Инициализируем параметры для Kafka
            Properties kafkaProperties = new Properties();
            kafkaProperties.put("bootstrap.servers", config.getProperty("kafka.bootstrap.servers"));
            kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProperties.put("group.id", "example-group");
            kafkaProperties.put("auto.offset.reset", "earliest");

            // Получаем имена из конфигурации
            String artemisQueue = config.getProperty("artemis.queue.name");
            String kafkaTopic = config.getProperty("kafka.topic.name");
            String message = config.getProperty("message.content");

            // Создаем сервис
            MessagingService messagingService = new MessagingService(
                    artemisBrokerURL, artemisUsername, artemisPassword, kafkaProperties, kafkaTopic
            );

            // Отправляем сообщение
            messagingService.sendMessage(artemisQueue, kafkaTopic, message);

            // Запускаем потребителей
            messagingService.startConsuming(artemisQueue);

            // Удерживаем основной поток
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ArtemisConnectionManager.closeConnection();
                KafkaConnectionManager.closeProducer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
