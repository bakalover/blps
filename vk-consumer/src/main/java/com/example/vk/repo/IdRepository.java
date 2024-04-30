package com.example.vk.repo;

import com.example.vk.repo.entity.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdRepository extends JpaRepository<Id, Long> {
}