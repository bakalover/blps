package com.example.blps.repo;

import com.example.blps.repo.entity.Album;
import com.example.blps.repo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByName(String name);

    void deleteAllByUser(User user);

}
