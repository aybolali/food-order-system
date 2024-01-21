package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExceptionClass;

public class RestaurantApplicationServiceException extends DomainExceptionClass {
    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
