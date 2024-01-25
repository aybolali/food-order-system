package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import outbox.OutboxScheduler;
import outbox.OutboxStatus;
import saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestaurantApprovalOutboxCleanerScheduler implements OutboxScheduler {
    private final ApprovalOutboxHelper helper;

    public RestaurantApprovalOutboxCleanerScheduler(ApprovalOutboxHelper helper) {
        this.helper = helper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessages =
                helper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED, //100% finished operation, failed are not in the final state, need to resolved
                        SagaStatus.SUCCEEDED,
                        SagaStatus.COMPENSATED,
                        SagaStatus.FAILED
                );
        if(outboxMessages.isPresent()){
            List<OrderApprovalOutboxMessage> response = outboxMessages.get();
            log.info("Received {} OrderApprovalOutboxMessages for clean-up. The payloads : {} ",
                    response.size(),
                    response.stream().map(
                            OrderApprovalOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            helper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.COMPENSATED,
                    SagaStatus.FAILED
            );
            log.info("{} OrderApprovalOutboxMessage deleted!", response.size());
        }
    }
}
