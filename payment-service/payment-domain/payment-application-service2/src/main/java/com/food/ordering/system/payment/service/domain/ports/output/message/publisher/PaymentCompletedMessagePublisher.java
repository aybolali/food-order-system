package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.PaymentCompletedEvent;
import domain.event.publisher.DomainEventPublisher;

public interface PaymentCompletedMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent> {
}
