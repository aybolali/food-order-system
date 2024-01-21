package com.food.ordering.system.domain.exception;

public class PaymentDomainException extends DomainExceptionClass {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
