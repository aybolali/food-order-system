package service.domain.exception;

import domain.exception.DomainExceptionClass;

public class RestaurantApplicationServiceException extends DomainExceptionClass {
    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
