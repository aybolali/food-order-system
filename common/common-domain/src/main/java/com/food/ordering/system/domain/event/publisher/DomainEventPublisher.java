package com.food.ordering.system.domain.event.publisher;

import com.food.ordering.system.domain.event.DomainEvents;

public interface DomainEventPublisher<T extends DomainEvents> {

    void publish(T domainEvent);
}
