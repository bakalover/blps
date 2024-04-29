package com.example.blps.repo;

import com.example.blps.repo.entity.Comment;
import com.example.blps.repo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByImage(Image image);
}
