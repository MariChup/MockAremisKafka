package org.example;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class ArtemisConnectionManager {
    private static Connection connection;

    public static synchronized Connection getConnection(String brokerURL, String username, String password) throws JMSException {
        if (connection == null) {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection(username, password);
            connection.start(); // Инициализация соединения
        }
        return connection;
    }

    public static void closeConnection() throws JMSException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
