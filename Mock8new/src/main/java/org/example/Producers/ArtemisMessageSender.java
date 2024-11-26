package org.example.Producers;

import org.example.Configs.ArtemisConnectionManager;

import javax.jms.*;

public class ArtemisMessageSender {
    private final String brokerURL;
    private final String username;
    private final String password;

    public ArtemisMessageSender(String brokerURL, String username, String password) {
        this.brokerURL = brokerURL;
        this.username = username;
        this.password = password;
    }

    public void sendMessageToQueue(String queueName, String message) {
        try {
            // Получаем соединение с сервером Artemis
            Connection connection = ArtemisConnectionManager.getConnection(brokerURL, username, password);
            // Создаем сессию для отправки сообщений
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Создаем очередь для отправки сообщений
            Destination destination = session.createQueue(queueName);
            // Создаем продюсера, который будет отправлять сообщения в очередь
            MessageProducer producer = session.createProducer(destination);

            // Создаем текстовое сообщение
            TextMessage textMessage = session.createTextMessage(message);
            // Отправляем сообщение в очередь
            producer.send(textMessage);

            System.out.println("Message sent to queue [" + queueName + "]: " + message);

            // Закрываем ресурсы
            producer.close();
            session.close();
        } catch (JMSException e) {
            // Обрабатываем исключение, например, выводим стек ошибки
            e.printStackTrace();
        }
    }

    // Геттеры для параметров
    public String getBrokerURL() {
        return brokerURL;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
