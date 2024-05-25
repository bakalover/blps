package com.example.blps.delegate.image;

import com.example.blps.repo.ComplaintRepository;
import com.example.blps.repo.entity.Complaint;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;


@Component
@Slf4j
public class ComplaintFetcherDelegate implements JavaDelegate {
    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<String> groups = delegateExecution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getGroupIds();
        if (!groups.contains("moderators") && !groups.contains("camunda-admin")) {
            runtimeService.deleteProcessInstance(delegateExecution.getProcessInstanceId(), "У вас нет прав совершать данное действие!");
            return;
        }

        transactionTemplate.execute(status -> {
            var complaints = complaintRepository.findAll();

            if (complaints.isEmpty()) {
                runtimeService.deleteProcessInstance(delegateExecution.getProcessInstanceId(), "Как хорошо, когда никто не нарушает правила =)");
            } else {
                StringBuilder description = new StringBuilder("Жалобы:");
                for (Complaint complaint : complaints) {
                    description.append("\n--------------------------------------------------\n").append(complaint.getDescription()).append("; Username: ").append(complaint.getUsername());
                }
                delegateExecution.setVariable("textarea_result", description.toString());
            }
            return null;
        });
    }
}
