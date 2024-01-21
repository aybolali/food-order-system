package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class orderPaidEvent extends OrderEvents {

    public orderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }

}
