package domain.event;

import domain.entity.OrderApproval;
import domain.event.publisher.DomainEventPublisher;
import domain.valueObject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent{
    private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher;

    public OrderRejectedEvent(OrderApproval orderApproval, RestaurantID restaurantId, List<String> failureMessages, ZonedDateTime createdAt, DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderRejectedEventDomainEventPublisher = orderRejectedEventDomainEventPublisher;
    }


    @Override
    public void fire() {
        orderRejectedEventDomainEventPublisher.publish(this);
    }
}
