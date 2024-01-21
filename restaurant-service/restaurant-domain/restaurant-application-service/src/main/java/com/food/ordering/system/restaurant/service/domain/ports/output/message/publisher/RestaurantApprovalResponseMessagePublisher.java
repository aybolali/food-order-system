package com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {
    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
