package com.example.blps.delegate.image;

import com.example.blps.repo.ImageRepository;
import com.example.blps.repo.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
public class ImageFetcherDelegate implements JavaDelegate {

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        transactionTemplate.execute(status -> {
            var images = imageRepository.findAll();

            if (images.isEmpty()) {
                delegateExecution.setVariable("textarea_result", "К сожалению не на что жаловаться =(");
            } else {
                StringBuilder description = new StringBuilder("Доступные для жалобы изображения:");
                for (Image image : images) {
                    description.append("\n---------------------------\n").append(image.getName()).append("; Id: ").append(image.getId());
                }
                delegateExecution.setVariable("textarea_result", description.toString());
            }
            return null;
        });
    }
}
