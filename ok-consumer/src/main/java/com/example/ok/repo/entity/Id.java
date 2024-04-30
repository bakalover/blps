package com.example.ok.repo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ok_ids")
public class Id {
    @jakarta.persistence.Id
    @Column(name = "image_id")
    private Long id;
}
