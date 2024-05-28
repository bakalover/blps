package com.example.blps.delegate.user;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.StatisticsRepository;
import com.example.blps.repo.entity.Image;
import com.example.blps.repo.entity.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@Slf4j
public class StatisticsDelegate implements JavaDelegate {

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<String> users = delegateExecution.
                getProcessEngineServices().
                getIdentityService().
                createUserQuery().
                list().stream().map(User::getId).toList();

        transactionTemplate.execute(status -> {
            users.forEach(username -> {
                var opt = statisticsRepository.findByUsername(username);
                var statistics = opt.orElseGet(Statistics::new);
                var albums = albumRepository.findAllByUsername(username);
                var images = albums.stream().flatMap(a -> a.getImages().stream()).toList();
                var vkLikesTotal = images.stream().mapToInt(Image::getVkLikes).sum();
                var okLikesTotal = images.stream().mapToInt(Image::getOkLikes).sum();
                statistics.setUsername(username);
                statistics.setAlbumCount(albums.size());
                statistics.setImageCount(images.size());
                statistics.setVkLikesCount(vkLikesTotal);
                statistics.setOkLikesCount(okLikesTotal);
                statisticsRepository.save(statistics);
            });
            return null;
        });
        log.info("User statistics updated!");
    }
}
