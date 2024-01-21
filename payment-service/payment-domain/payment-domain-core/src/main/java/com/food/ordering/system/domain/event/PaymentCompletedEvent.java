package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvents{
    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }


}
