package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Payment;
import domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvents{
    private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher;
    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
    }


    @Override
    public void fire() {
        paymentCancelledEventDomainEventPublisher.publish(this);
    }
}
