package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class orderPaidEvent extends OrderEvents {
    private final DomainEventPublisher<orderPaidEvent> orderPaidEventDomainEventPublisher;

    public orderPaidEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<orderPaidEvent> orderPaidEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderPaidEventDomainEventPublisher = orderPaidEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderPaidEventDomainEventPublisher.publish(this);
    }
}
