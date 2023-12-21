package domain;

import domain.entity.Restaurant;
import domain.event.OrderApprovalEvent;
import domain.event.OrderApprovedEvent;
import domain.event.OrderRejectedEvent;
import domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface RestaurantDomainService {
    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                     DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);
}
