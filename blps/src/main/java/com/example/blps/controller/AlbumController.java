package com.example.blps.controller;

//import com.example.blps.repo.entity.Album;
//import com.example.blps.repo.request.AlbumBody;
//import com.example.blps.service.AlbumService;
//import org.hibernate.TransactionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.NoSuchElementException;
//
//@RestController
//@RequestMapping("/album")
//public class AlbumController {
//
//    private final String okMsg = "Ok\n";
//
//    @Autowired
//    private AlbumService albumService;
//
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addAlbum(@RequestBody AlbumBody album) {
//        try {
//            albumService.addNewAlbum(album);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Album already exist!\n");
//        }
//        return ResponseEntity.ok().body(okMsg);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {
//        try {
//            albumService.deleteAlbumById(id);
//            return ResponseEntity.ok().body(okMsg);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.badRequest().body("Cannot find specified album!\n");
//        }
//    }
//
//    @PostMapping("/move")
//    public ResponseEntity<String> movePics(@RequestParam Long from, @RequestParam Long to,
//                                           @RequestBody ArrayList<Long> ids) {
//        try {
//            albumService.moveImages(from, to, ids);
//            return ResponseEntity.ok().body(okMsg);
//
//        } catch (TransactionException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
//        try {
//            var album = albumService.getAlbum(id);
//            return ResponseEntity.ok().body(album);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//}
