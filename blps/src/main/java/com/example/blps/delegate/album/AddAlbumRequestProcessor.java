package com.example.blps.delegate.album;

import com.example.blps.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddAlbumRequestProcessor implements JavaDelegate {

    @Autowired
    AlbumService albumService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = delegateExecution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();
        String albumName = delegateExecution.getVariable("album_name").toString();
        String albumDescription = delegateExecution.getVariable("album_description").toString();
        log.info("Пользователь: {} активировал процесс добавление альбома", username);
        try {
            albumService.addNewAlbum(albumName, albumDescription, username);
            delegateExecution.setVariable("textarea_result", "Ok");
        } catch (IllegalArgumentException e) {
            log.info(e.toString());
            delegateExecution.setVariable("textarea_result", "Album already exists!");
        }
    }
}
