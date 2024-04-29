package com.example.blps.service;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.ImageRepository;
import com.example.blps.repo.UserRepository;
import com.example.blps.repo.entity.Album;
import com.example.blps.repo.entity.Image;
import com.example.blps.repo.request.AlbumBody;
import lombok.NonNull;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service("album_service")
public class AlbumService {

    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("null")
    @Autowired
    public AlbumService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(1);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
    }

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public void addNewAlbum(AlbumBody album) throws IllegalArgumentException {
        if (!albumRepository.findByName(album.getName()).isEmpty()) {
            throw new IllegalArgumentException();
        }
        var albumDao = new Album();
        albumDao.setDescription(album.getDescription());
        albumDao.setName(album.getName());
        albumDao.setRestrictMode(album.getRestrictMode());
        albumDao.setUser(userRepository.findByUsername(album.getUsername()).get());
        albumRepository.save(albumDao);
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
