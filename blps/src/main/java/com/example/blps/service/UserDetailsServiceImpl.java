package com.example.blps.service;

import com.example.blps.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username '" + username + "'"));
        var userDetails = new UserDetailsImpl();
        userDetails.setHashedPasswd(user.getHashedPassword());
        userDetails.setUsername(user.getUsername());
        userDetails.setRoles(user.getRoles());
        return userDetails;

    }
}