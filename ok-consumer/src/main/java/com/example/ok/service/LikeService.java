package com.example.ok.service;

import com.example.ok.kafka.KafkaSender;
import com.example.ok.repo.IdRepository;
import com.example.ok.repo.entity.KafkaLikes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LikeService {

    @Autowired
    private IdRepository idRepository;

    @Autowired
    private KafkaSender kafkaSender;

    private final Random twister = new Random();

    @Scheduled(fixedRate = 5000)
    @Async
    public void produceLikes() {
        idRepository.findAll().forEach(id ->
        {
            var kl = new KafkaLikes();
            kl.setServiceType("ok");
            kl.setImageId(id.getId());
            kl.setLikeDelta(twister.nextInt(1, 10));
            kafkaSender.send(kl);
        });
    }
}
