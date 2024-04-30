package com.example.blps.kafka;

import com.example.blps.repo.ImageRepository;
import com.example.blps.repo.entity.KafkaLikes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListener {
    @Autowired
    private ImageRepository imageRepository;

    @org.springframework.kafka.annotation.KafkaListener(groupId = "group-blps", topics = "topic-likes")
    void listenConsume(KafkaLikes data) {
        log.info("Recieve: {}", data.toString());
        var imageOpt = imageRepository.findById(data.getImageId()); // Happens-Before => safe
        if (imageOpt.isPresent()) {
            var image = imageOpt.get();
            if (data.getServiceType().equals("vk")) {
                image.setVkLikes(image.getVkLikes() + data.getLikeDelta());
            } else {
                image.setOkLikes(image.getOkLikes() + data.getLikeDelta());
            }
            imageRepository.save(image);
        }
    }
}