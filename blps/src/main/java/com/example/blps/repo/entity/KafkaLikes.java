package com.example.blps.repo.entity;

import lombok.Data;

@Data
public class KafkaLikes {
    private String serviceType;
    private Long imageId;
    private Integer likeDelta;
}
