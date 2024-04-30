package com.example.ok.repo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KafkaLikes {
    private String serviceType;
    private Long imageId;
    private Integer likeDelta;
}
