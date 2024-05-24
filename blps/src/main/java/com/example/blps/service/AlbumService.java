package com.example.blps.service;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.ImageRepository;
import com.example.blps.repo.entity.Album;
import com.example.blps.repo.entity.Image;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service("album_service")
@Slf4j
public class AlbumService {

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageRepository imageRepository;


    public void addNewAlbum(String name, String description, String username) {
        log.info("Альбом: {}", name);
        transactionTemplate.execute(status -> {
            if (!albumRepository.findByName(name).isEmpty()) {
                throw new IllegalArgumentException();
            }
            var albumDao = new Album();
            albumDao.setDescription(description);
            albumDao.setName(name);
            albumDao.setUsername(username);
            albumRepository.save(albumDao);
            return null;
        });
    }

    public void deleteAlbumById(@NonNull Long id) throws NoSuchElementException {
        albumRepository.findById(id).orElseThrow();
        albumRepository.deleteById(id);
    }

    public void moveImages(@NonNull Long fromId, @NonNull Long toId, List<Long> ids) throws NoSuchElementException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@SuppressWarnings("null") TransactionStatus status) {
                try {
                    Album from = albumRepository.findById(fromId).orElseThrow();
                    Album to = albumRepository.findById(toId).orElseThrow();
                    List<Image> toMove = imageRepository.findByAlbum(from);
                    List<Image> alreadyHave = imageRepository.findByAlbum(from);
                    alreadyHave.forEach(image -> {
                        if (image.getFace()) {
                            image.setFace(false);
                            imageRepository.save(image);
                        }
                    });
                    toMove.forEach(image -> image.setAlbum(to));
                    List<Image> toMoveFiltered = new ArrayList<>();
                    for (Image image : toMove) {
                        if (ids.contains(image.getId())) {
                            toMoveFiltered.add(image);
                        }
                    }
                    imageRepository.saveAll(toMoveFiltered);
                } catch (NoSuchElementException e) {
                    status.setRollbackOnly();
                    throw new TransactionException("Cannot move elements!\n");
                }
            }

        });

    }

    public Album getAlbum(@NonNull Long id) throws NoSuchElementException {
        return albumRepository.findById(id).orElseThrow();
    }
}
