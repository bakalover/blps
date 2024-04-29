package com.example.blps.repo;

import com.example.blps.repo.entity.Complaint;
import com.example.blps.repo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByImage(Image image);
}
