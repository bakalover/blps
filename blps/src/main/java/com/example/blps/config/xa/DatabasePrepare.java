package com.example.blps.config.xa;

import com.example.blps.repo.RoleRepository;
import com.example.blps.repo.UserRepository;
import com.example.blps.repo.entity.Role;
import com.example.blps.repo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabasePrepare implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var roleUser = new Role();
        var roleModerator = new Role();
        var roleAdmin = new Role();
        roleUser.setName("ROLE_USER");
        roleModerator.setName("ROLE_MODERATOR");
        roleAdmin.setName("ROLE_ADMIN");
        roleRepository.saveAll(List.of(roleUser, roleModerator, roleAdmin));

        var moderator = new User();
        moderator.setRoles(List.of(roleModerator));
        moderator.setUsername("moderator");
        moderator.setHashedPassword(passwordEncoder.encode("moderator"));

        var admin = new User();
        admin.setRoles(List.of(roleAdmin));
        admin.setUsername("admin");
        admin.setHashedPassword(passwordEncoder.encode("admin"));

        userRepository.saveAll(List.of(moderator, admin));
    }
}
