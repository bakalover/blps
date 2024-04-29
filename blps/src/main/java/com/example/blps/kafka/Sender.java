package com.example.blps.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        var message1 = "Hello Kafka on partition-0!";
        var message2 = "Hello Kafka on partition-1!";
        var topicName = "topic-0";
        kafkaTemplate.send(topicName, 0, null, message1);
        kafkaTemplate.send(topicName, 1, null, message2);

    }
}