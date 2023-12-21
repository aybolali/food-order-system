package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.PaymentFailedEvent;
import domain.event.publisher.DomainEventPublisher;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
