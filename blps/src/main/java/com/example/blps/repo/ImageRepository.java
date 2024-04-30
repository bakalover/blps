package com.example.blps.repo;

import com.example.blps.repo.entity.Album;
import com.example.blps.repo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByAlbum(Album album);

    Optional<Image> findFirstByOrderByIdDesc();
}