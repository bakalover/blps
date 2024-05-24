package com.example.blps.service;

//import com.example.blps.kafka.KafkaSender;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.CommentRepository;
import com.example.blps.repo.ComplaintRepository;
import com.example.blps.repo.ImageRepository;
import com.example.blps.repo.entity.Album;
import com.example.blps.repo.entity.Comment;
import com.example.blps.repo.entity.Complaint;
import com.example.blps.repo.entity.Image;
import com.example.blps.repo.request.CommentBody;
import com.example.blps.repo.request.ComplaintBody;
import lombok.NonNull;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Service("image_service")
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private ComplaintRepository complaintRepository;

//    @Autowired
//    private KafkaSender kafkaSender;

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    public void addNewImage(String albumName, String imageName, Boolean face)
            throws TransactionException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(@SuppressWarnings("null") TransactionStatus status) {
                try {
                    var newImage = new Image();
                    var album = albumRepository.findByName(albumName).orElseThrow();

                    newImage.setAlbum(album);
                    newImage.setName(imageName);
                    newImage.setFace(face);
                    newImage.setVkLikes(0);
                    newImage.setOkLikes(0);

                    var images = imageRepository.findByAlbum(album);
                    images.forEach(image -> {
                        if (image.getFace()) {
                            image.setFace(false);
                            imageRepository.save(image);
                        }
                    });

                    imageRepository.save(newImage);
                    var savedImage = imageRepository.findFirstByOrderByIdDesc();

                    if (savedImage.isEmpty()) {
                        throw new NoSuchElementException("Cannot restore image before sending to outsource");
                    }

                    var id = savedImage.get().getId();
//                    kafkaSender.send(id, 0);    // VK
//                    kafkaSender.send(id, 1);    // OK
                } catch (NoSuchElementException e) {
                    status.setRollbackOnly();
                    throw new TransactionException("Album doesn't exist!\n");
                }
            }

        });
    }

    public Image findById(@NonNull Long id) throws NoSuchElementException {
        return imageRepository.findById(id).orElseThrow();
    }

    public List<Image> findByAlbum(Album album) {
        return imageRepository.findByAlbum(album);
    }

    public void deleteById(@NonNull Long id) throws NoSuchElementException {
        imageRepository.findById(id).orElseThrow();
        imageRepository.deleteById(id);
    }

    public void addComment(CommentBody comment) throws NoSuchElementException {
        var dao = new Comment();
        dao.setImage(imageRepository.findById(comment.getPicId()).orElseThrow());
        dao.setText(comment.getText());
        dao.setUsername(comment.getUsername());
        commentRepository.save(dao);
    }

    public List<Comment> getComments(@NonNull Long picId) throws NoSuchElementException {
        return commentRepository.findByImage(imageRepository.findById(picId).orElseThrow());
    }

    public void makeComplaint(ComplaintBody complaintBody) throws NoSuchElementException {
        var complaint = new Complaint();
        complaint.setDescription(complaintBody.getDescription());
        complaint.setImage(imageRepository.findById(complaintBody.getPicId()).get());
        complaint.setUsername(complaintBody.getUsername());
        complaintRepository.save(complaint);
    }

    public List<Complaint> getComplaint(@NonNull Long picId) throws NoSuchElementException {
        return complaintRepository.findByImage(imageRepository.findById(picId).get());
    }
}
