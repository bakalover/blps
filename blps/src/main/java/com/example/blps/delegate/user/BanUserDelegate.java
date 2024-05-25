package com.example.blps.delegate.user;

import com.example.blps.repo.AlbumRepository;
import com.example.blps.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@Slf4j
public class BanUserDelegate implements JavaDelegate {

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<String> groups = delegateExecution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getGroupIds();
        if (!groups.contains("camunda-admin")) {
            runtimeService.deleteProcessInstance(delegateExecution.getProcessInstanceId(), "У вас нет прав совершать данное действие!");
            return;
        }
        String username = delegateExecution.getVariable("username_to_ban").toString();
        transactionTemplate.execute(status -> {
            albumRepository.deleteAllByUsername(username);
            return null;
        });
        final IdentityServiceImpl identityService = (IdentityServiceImpl) delegateExecution.getProcessEngineServices().getIdentityService();
        identityService.createMembership(username, "FOREVERBURNINHELL");
    }
}
