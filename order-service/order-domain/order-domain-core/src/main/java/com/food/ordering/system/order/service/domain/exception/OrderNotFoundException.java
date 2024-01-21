package com.food.ordering.system.order.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExceptionClass;

public class OrderNotFoundException extends DomainExceptionClass {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
