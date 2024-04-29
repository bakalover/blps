package com.example.blps.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

    @KafkaListener(groupId = "group-0", topicPartitions
            = @TopicPartition(topic = "topic-0", partitions = {"0"}))
    void listener1(String data) {
        log.info("Receiving via Kafka on partition-0: {}", data);
    }

    @KafkaListener(groupId = "group-0", topicPartitions
            = @TopicPartition(topic = "topic-0", partitions = {"1"}))
    void listener2(String data) {
        log.info("Receiving via Kafka on partition-1: {}", data);
    }


}