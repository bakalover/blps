package com.example.blps.delegate.image;

import com.example.blps.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class ComplaintProcessorDelegate implements JavaDelegate {

    @Autowired
    private ImageService imageService;

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            Long picId = Long.valueOf(delegateExecution.getVariable("pic_id").toString());
            String username = delegateExecution.getVariable("username").toString();
            String description = delegateExecution.getVariable("description").toString();

            transactionTemplate.execute(status -> {
                        imageService.makeComplaint(picId, username, description);
                        return null;
                    }
            );
            delegateExecution.setVariable("textarea_result", "Ok");
        } catch (NumberFormatException e) {
            delegateExecution.setVariable("textarea_result", "Некорректный идентификатор изображения");
        } catch (NoSuchElementException e) {
            delegateExecution.setVariable("textarea_result", "По выбраному идентификатору не было найдено изображений");

        }

    }
}
