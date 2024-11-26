package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class MessagingController {

    private final MessagingService messagingService;

    @Autowired
    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping("/artemis")
    public String sendMessageToArtemis(
            @RequestParam("message") String message,
            @RequestParam("artemisQueue") String artemisQueue,   // Параметр для имени очереди Artemis
            @RequestParam("kafkaTopic") String kafkaTopic) {    // Параметр для имени топика Kafka

        // Вызов сервиса для отправки сообщения
        messagingService.sendMessage(artemisQueue, kafkaTopic, message);

        return "Message sent to Artemis Queue: " + artemisQueue + " and Kafka Topic: " + kafkaTopic + " with message: " + message;
    }
}