package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class PaymentEvents implements DomainEvents<Payment> {
    private final Payment payment;
    private final ZonedDateTime createdAt;

    private final List<String> failureMessages;

    protected PaymentEvents(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
