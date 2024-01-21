package com.food.ordering.system.domain.exception;

public class PaymentNotFoundException extends DomainExceptionClass {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
