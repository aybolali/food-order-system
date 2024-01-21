package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
