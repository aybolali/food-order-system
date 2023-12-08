package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import domain.event.DomainEvents;

import java.time.ZonedDateTime;

public abstract class OrderEvents implements DomainEvents<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvents(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
