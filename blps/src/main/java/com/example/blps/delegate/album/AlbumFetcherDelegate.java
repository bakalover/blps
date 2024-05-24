package com.example.blps.delegate.album;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.entity.Album;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
public class AlbumFetcherDelegate implements JavaDelegate {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = delegateExecution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();

        transactionTemplate.execute(status -> {
            var albums = albumRepository.findAllByUsername(username);

            if (albums.isEmpty()) {
                delegateExecution.setVariable("textarea_result", "К сожалению у вас нет альбомов =(");
            } else {
                StringBuilder description = new StringBuilder("Ваши альбомы:");
                for (Album album : albums) {
                    description.append("\n---------------------------\n").append(album.getName());
                }
                delegateExecution.setVariable("textarea_result", description.toString());
            }
            return null;
        });
    }
}
