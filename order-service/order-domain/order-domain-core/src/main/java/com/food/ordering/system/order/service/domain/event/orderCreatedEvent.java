package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class orderCreatedEvent extends OrderEvents {

    private final DomainEventPublisher<orderCreatedEvent> orderCreatedEventDomainEventPublisher;

    public orderCreatedEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<orderCreatedEvent> orderCreatedEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderCreatedEventDomainEventPublisher = orderCreatedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderCreatedEventDomainEventPublisher.publish(this);
    }
}
