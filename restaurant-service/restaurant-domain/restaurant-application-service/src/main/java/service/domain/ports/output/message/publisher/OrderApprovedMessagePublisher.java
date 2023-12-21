package service.domain.ports.output.message.publisher;

import domain.event.OrderApprovedEvent;
import domain.event.publisher.DomainEventPublisher;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {

}
