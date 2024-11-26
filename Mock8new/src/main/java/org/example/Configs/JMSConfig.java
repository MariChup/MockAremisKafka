package org.example.Configs;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

@Configuration
public class JMSConfig {

    @Bean
    public ActiveMQConnectionFactory artemisConnectionFactory(
            @Value("${artemis.broker.url}") String brokerURL,
            @Value("${artemis.username}") String username,
            @Value("${artemis.password}") String password) {
        return new ActiveMQConnectionFactory(brokerURL, username, password);
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean
    public void createArtemisQueue(ActiveMQConnectionFactory connectionFactory,
                                   @Value("${artemis.queue.name}") String queueName) throws JMSException {
        try (Connection connection = connectionFactory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.createQueue(queueName);
        }
    }
}
