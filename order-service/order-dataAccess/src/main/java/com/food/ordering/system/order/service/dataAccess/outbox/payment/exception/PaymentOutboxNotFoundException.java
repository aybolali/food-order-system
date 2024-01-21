package com.food.ordering.system.order.service.dataAccess.outbox.payment.exception;

public class PaymentOutboxNotFoundException extends RuntimeException {

    public PaymentOutboxNotFoundException(String message) {
        super(message);
    }
}
