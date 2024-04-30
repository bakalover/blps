package com.example.blps.kafka;

import com.example.blps.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListener {
    @Autowired
    private ImageRepository imageRepository;

    @org.springframework.kafka.annotation.KafkaListener(groupId = "group-blps", topics = "topic-blps")
    void listenConsume(Pair<String, Pair<Long, Integer>> data) {
        log.info("Receiving data via kafka: {}", data);
        var imageId = data.getSecond().getFirst();
        var serviceType = data.getFirst();
        var likesDelta = data.getSecond().getSecond();

        var image = imageRepository.findById(imageId).get(); // Happens-Before => safe
        if (serviceType.equals("vk")) {
            image.setVkLikes(image.getVkLikes() + likesDelta);
        } else {
            image.setOkLikes(image.getOkLikes() + likesDelta);
        }

    }
}