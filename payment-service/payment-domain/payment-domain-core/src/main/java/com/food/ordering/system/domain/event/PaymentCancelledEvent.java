package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvents{
    public PaymentCancelledEvent(Payment payment,
                                 ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }

}
