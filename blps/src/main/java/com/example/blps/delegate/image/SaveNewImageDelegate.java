package com.example.blps.delegate.image;

import com.example.blps.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class SaveNewImageDelegate implements JavaDelegate {

    @Autowired
    ImageService imageService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String albumName = delegateExecution.getVariable("album_name").toString();
        String imageName = delegateExecution.getVariable("image_name").toString();
        String isFace = delegateExecution.getVariable("is_face").toString();
        try {
            imageService.addNewImage(albumName, imageName, Objects.equals(isFace, "true"));
            delegateExecution.setVariable("textarea_result", "Ok");
        } catch (Exception e) {
            delegateExecution.setVariable("textarea_result", e.getMessage());
        }
    }
}
