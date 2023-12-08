package com.food.ordering.system.order.service.domain.exception;

import domain.exception.DomainExceptionClass;

public class OrderDomainException extends DomainExceptionClass {
    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
