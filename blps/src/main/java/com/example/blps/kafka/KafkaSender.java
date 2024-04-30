package com.example.blps.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    private final String topicName = "topic-images";

    public void send(Long what, Integer partition) {
        log.info("Sending: {} to partition: {}", what, partition);
        kafkaTemplate.send(topicName, partition, "NULL_KEY", what); // OK
    }
}