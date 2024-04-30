package com.example.blps.repo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "album_count", nullable = false)
    private Integer albumCount;

    @Column(name = "image_count", nullable = false)
    private Integer imageCount;

    @Column(name = "vk_likes_count", nullable = false)
    private Integer vkLikesCount;

    @Column(name = "ok_likes_count", nullable = false)
    private Integer okLikesCount;
}
