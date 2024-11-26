package org.example.Consumers;

import javax.jms.*;

public class ArtemisMessageConsumer {
    private final Connection connection;
    private final String queueName;

    public ArtemisMessageConsumer(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
    }

    public void consumeMessages() {
        try {
            // Создаем сессию для получения сообщений
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Создаем очередь для получения сообщений
            Destination destination = session.createQueue(queueName);
            // Создаем консьюмера для чтения сообщений
            MessageConsumer consumer = session.createConsumer(destination);

            System.out.println("Listening for messages on Artemis queue: " + queueName);

            // Устанавливаем слушателя сообщений
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("Received message from Artemis queue [" + queueName + "]: " + textMessage.getText());
                    } else {
                        System.out.println("Received non-text message from Artemis queue [" + queueName + "]");
                    }
                } catch (JMSException e) {
                    e.printStackTrace(); // Обработка исключения внутри слушателя
                }
            });
        } catch (JMSException e) {
            e.printStackTrace(); // Обработка исключения при создании сессии или потребителя
        }
    }
}
