package com.example.blps.delegate.user;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationRequestProcessor implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String userId = delegateExecution.getVariable("user_id").toString();
        String username = delegateExecution.getVariable("username").toString();
        String userPassword = delegateExecution.getVariable("password").toString();
        final IdentityServiceImpl identityService = (IdentityServiceImpl) delegateExecution.getProcessEngineServices().getIdentityService();

        if (identityService.createUserQuery().userId(userId).count() != 0) {
            runtimeService.deleteProcessInstance(delegateExecution.getProcessInstanceId(), "User id=" + userId + " already exists!");
            return;
        }
        User newUser = identityService.newUser(userId);
        newUser.setFirstName(username);
        newUser.setPassword(userPassword);
        newUser.setEmail(userId + "@stranamam.ru");
        identityService.saveUser(newUser, true);
        identityService.createMembership(userId, "authorized");

        delegateExecution.setVariable("textarea_register_result", "Succeed");
    }
}