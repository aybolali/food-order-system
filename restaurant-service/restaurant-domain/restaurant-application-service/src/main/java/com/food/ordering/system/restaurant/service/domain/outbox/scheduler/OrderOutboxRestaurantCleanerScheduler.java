package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import outbox.OutboxScheduler;
import outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderOutboxRestaurantCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxRestaurantHelper orderOutboxRestaurantHelper;

    public OrderOutboxRestaurantCleanerScheduler(OrderOutboxRestaurantHelper orderOutboxRestaurantHelper) {
        this.orderOutboxRestaurantHelper = orderOutboxRestaurantHelper;
    }

    @Transactional
    @Scheduled(cron = "@midnight")
    @Override
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxRestaurantHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());
            orderOutboxRestaurantHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("Deleted {} OrderOutboxMessage!", outboxMessages.size());
        }
    }
}