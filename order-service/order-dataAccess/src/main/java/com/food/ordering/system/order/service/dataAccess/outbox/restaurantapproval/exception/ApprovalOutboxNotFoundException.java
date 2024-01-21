package com.food.ordering.system.order.service.dataAccess.outbox.restaurantapproval.exception;

public class ApprovalOutboxNotFoundException extends RuntimeException {

    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}
