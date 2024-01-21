package com.food.ordering.system.payment.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExceptionClass;

public class PaymentApplicationServiceException extends DomainExceptionClass {
    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
