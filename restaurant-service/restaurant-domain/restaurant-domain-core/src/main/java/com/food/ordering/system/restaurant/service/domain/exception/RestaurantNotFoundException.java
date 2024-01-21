package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExceptionClass;

public class RestaurantNotFoundException extends DomainExceptionClass {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
