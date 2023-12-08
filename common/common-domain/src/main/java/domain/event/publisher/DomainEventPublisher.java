package domain.event.publisher;

import domain.event.DomainEvents;

public interface DomainEventPublisher<T extends DomainEvents> {

    void publish(T domainEvent);
}
