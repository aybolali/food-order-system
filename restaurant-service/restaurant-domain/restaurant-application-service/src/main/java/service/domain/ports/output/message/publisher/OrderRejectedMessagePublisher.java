package service.domain.ports.output.message.publisher;

import domain.event.OrderRejectedEvent;
import domain.event.publisher.DomainEventPublisher;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
