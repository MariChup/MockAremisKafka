package org.example;

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

    public void sendMessageToQueue(String queueName, String message) throws JMSException {
        Connection connection = ArtemisConnectionManager.getConnection(brokerURL, username, password);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(destination);

        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);

        System.out.println("Message sent to queue [" + queueName + "]: " + message);

        producer.close();
        session.close();
    }

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
