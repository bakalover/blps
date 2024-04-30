package com.example.ok.kafka;

import com.example.ok.repo.IdRepository;
import com.example.ok.repo.entity.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListener {
    @Autowired
    private IdRepository idRepository;

    @org.springframework.kafka.annotation.KafkaListener(groupId = "group-social", topicPartitions
            = @TopicPartition(topic = "topic-images", partitions = {"1"}))
    void listenConsume(Long imageId) {
        if (idRepository.findById(imageId).isPresent()) {
            idRepository.deleteById(imageId);
        }
        var id = new Id();
        id.setId(imageId);
        idRepository.save(id);
    }
}