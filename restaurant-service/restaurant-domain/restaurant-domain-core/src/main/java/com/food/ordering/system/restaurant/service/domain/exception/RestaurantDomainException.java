package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExceptionClass;

public class RestaurantDomainException extends DomainExceptionClass {
    public RestaurantDomainException(String message) {
        super(message);
    }

    public RestaurantDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
