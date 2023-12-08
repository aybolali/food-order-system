package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.event.orderCreatedEvent;
import domain.event.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements
        ApplicationEventPublisherAware,
        DomainEventPublisher<orderCreatedEvent> {

    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publish(orderCreatedEvent domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        log.info("orderCreatedEvent is published for order id: {}", domainEvent.getOrder().getId().getValue());

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
