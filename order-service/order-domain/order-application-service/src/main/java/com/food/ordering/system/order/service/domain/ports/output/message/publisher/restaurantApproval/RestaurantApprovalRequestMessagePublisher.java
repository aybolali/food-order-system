package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantApproval;

import com.food.ordering.system.order.service.domain.outbox.module.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.module.payment.OrderPaymentOutboxMessage;
import outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}
