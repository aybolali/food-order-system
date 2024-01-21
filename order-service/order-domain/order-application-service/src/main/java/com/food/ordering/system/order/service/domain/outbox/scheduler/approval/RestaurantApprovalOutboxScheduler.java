package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordering.system.order.service.domain.outbox.module.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantApproval.RestaurantApprovalRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import outbox.OutboxScheduler;
import outbox.OutboxStatus;
import saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    public RestaurantApprovalOutboxScheduler(ApprovalOutboxHelper approvalOutboxHelper, RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher) {
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.restaurantApprovalRequestMessagePublisher = restaurantApprovalRequestMessagePublisher;
    }

    @Override
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    @Transactional
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> orderApprovalOutboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.STARTED,
                        SagaStatus.PROCESSING //corresponds to order paid status
                );
        if (orderApprovalOutboxMessagesResponse.isPresent() && orderApprovalOutboxMessagesResponse.get().size() > 0) {
            List<OrderApprovalOutboxMessage> outboxMessages = orderApprovalOutboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage with ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            outboxMessage.getId().toString()).collect(Collectors.joining(",")));

            outboxMessages.forEach(orderApprovalOutboxMessage ->
                    restaurantApprovalRequestMessagePublisher.publish(orderApprovalOutboxMessage, this::updateOutboxStatus));
        }
    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OutboxStatus outboxStatus) {
        orderApprovalOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderApprovalOutboxMessage);
        log.info("OrderApprovalOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
