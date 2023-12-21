package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.PaymentCancelledEvent;
import domain.event.publisher.DomainEventPublisher;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
