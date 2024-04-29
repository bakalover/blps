package com.example.blps.service;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.RoleRepository;
import com.example.blps.repo.UserRepository;
import com.example.blps.repo.entity.User;
import com.example.blps.repo.request.UserBody;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;

@Configuration
@Service("user_service")
public class UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("null")
    @Autowired
    public UserService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(1);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
    }

    public void registerUser(UserBody user) throws Exception {
        if (user.getUsername().isEmpty() || user.getPasswdString().isEmpty()) {
            throw new Exception();
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception();
        }

        var hashedPasswd = encoder.encode(user.getPasswdString());
        var userDao = new User();
        userDao.setUsername(user.getUsername());
        userDao.setHashedPassword(hashedPasswd);
        userDao.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(userDao);
    }

    public void banUser(String username) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                try {
                    var user = userRepository.findByUsername(username).get();
                    albumRepository.deleteAllByUser(user);
                    user.setBlocked(true);
                    user.getRoles().clear();
                    userRepository.save(user);
                } catch (NoSuchElementException e) {
                    status.setRollbackOnly();
                }
            }

        });
    }
}
