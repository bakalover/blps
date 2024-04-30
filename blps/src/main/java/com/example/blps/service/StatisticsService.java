package com.example.blps.service;

import com.example.blps.repo.StatisticsRepository;
import com.example.blps.repo.UserRepository;
import com.example.blps.repo.entity.Image;
import com.example.blps.repo.entity.Statistics;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private UserRepository userRepository;

    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("null")
    @Autowired
    public StatisticsService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(1);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
    }


    @Scheduled(cron = "3 * * * * *")
    @Async
    public void updateStatistics() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                userRepository.findAll().forEach(user -> {
                    var opt = statisticsRepository.findByUsername(user.getUsername());
                    var statistics = opt.orElseGet(Statistics::new);
                    var albums = user.getAlbums();
                    var images = albums.stream().flatMap(a -> a.getImages().stream()).toList();
                    var vkLikesTotal = images.stream().mapToInt(Image::getVkLikes).sum();
                    var okLikesTotal = images.stream().mapToInt(Image::getOkLikes).sum();

                    statistics.setUsername(user.getUsername());
                    statistics.setAlbumCount(albums.size());
                    statistics.setImageCount(images.size());
                    statistics.setVkLikesCount(vkLikesTotal);
                    statistics.setOkLikesCount(okLikesTotal);
                    statisticsRepository.save(statistics);
                });
            }
        });
        log.info("User statistics updated!");
    }


}
