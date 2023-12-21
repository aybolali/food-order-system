package com.food.ordering.system.domain.exception;

import domain.exception.DomainExceptionClass;

public class PaymentDomainException extends DomainExceptionClass {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
