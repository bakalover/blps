package com.example.vk.kafka;

import com.example.vk.repo.entity.KafkaLikes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, KafkaLikes> kafkaTemplate;

    private final String topicName = "topic-likes";

    public void send(KafkaLikes what) {
        log.info("Sending: {}", what);
        kafkaTemplate.send(topicName, what); // OK
    }
}