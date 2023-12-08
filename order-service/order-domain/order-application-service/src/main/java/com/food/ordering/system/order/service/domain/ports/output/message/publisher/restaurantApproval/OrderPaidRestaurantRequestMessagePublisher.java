package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantApproval;

import com.food.ordering.system.order.service.domain.event.orderPaidEvent;
import domain.event.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<orderPaidEvent> {
}
