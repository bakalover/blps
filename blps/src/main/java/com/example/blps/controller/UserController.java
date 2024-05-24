package com.example.blps.controller;
//
//import com.example.blps.repo.request.UserBody;
////import com.example.blps.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    private final String okMsg = "Ok\n";
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody UserBody user) {
//        try {
//            userService.registerUser(user);
//            return ResponseEntity.ok().body(okMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Cannot register user!");
//        }
//    }
//
//    @PostMapping("/ban/{username}")
//    public ResponseEntity<String> register(@PathVariable String username) {
//        try {
//            userService.banUser(username);
//            return ResponseEntity.ok().body(okMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Cannot ban user!");
//        }
//    }
//
//}