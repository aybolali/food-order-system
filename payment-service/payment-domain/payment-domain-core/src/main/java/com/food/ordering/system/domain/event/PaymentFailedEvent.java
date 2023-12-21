package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Payment;
import domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvents{
    private final DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher;
    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
        super(payment, createdAt, failureMessages);
        this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        paymentFailedEventDomainEventPublisher.publish(this);
    }
}
