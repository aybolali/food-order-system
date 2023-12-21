package domain.event;

import domain.entity.OrderApproval;
import domain.event.publisher.DomainEventPublisher;
import domain.valueObject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent{
    private final DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher;


    public OrderApprovedEvent(OrderApproval orderApproval, RestaurantID restaurantId, List<String> failureMessages, ZonedDateTime createdAt, DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderApprovedEventDomainEventPublisher = orderApprovedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderApprovedEventDomainEventPublisher.publish(this);
    }
}
