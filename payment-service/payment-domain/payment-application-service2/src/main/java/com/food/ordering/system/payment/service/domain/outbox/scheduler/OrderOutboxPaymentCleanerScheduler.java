package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import outbox.OutboxScheduler;
import outbox.OutboxStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderOutboxPaymentCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxPaymentHelper orderOutboxPaymentHelper;

    public OrderOutboxPaymentCleanerScheduler(OrderOutboxPaymentHelper orderOutboxPaymentHelper) {
        this.orderOutboxPaymentHelper = orderOutboxPaymentHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxPaymentHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());
            orderOutboxPaymentHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("Deleted {} OrderOutboxMessage!", outboxMessages.size());
        }
    }
}
