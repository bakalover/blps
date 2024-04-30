package com.example.ok.repo;

import com.example.ok.repo.entity.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdRepository extends JpaRepository<Id, Long> {
}