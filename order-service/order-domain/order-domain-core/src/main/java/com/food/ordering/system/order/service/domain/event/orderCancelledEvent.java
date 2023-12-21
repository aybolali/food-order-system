package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class orderCancelledEvent extends OrderEvents {
    private final DomainEventPublisher<orderCancelledEvent> orderCancelledEventDomainEventPublisher;

    public orderCancelledEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<orderCancelledEvent> orderCancelledEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventDomainEventPublisher = orderCancelledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderCancelledEventDomainEventPublisher.publish(this);
    }
}
