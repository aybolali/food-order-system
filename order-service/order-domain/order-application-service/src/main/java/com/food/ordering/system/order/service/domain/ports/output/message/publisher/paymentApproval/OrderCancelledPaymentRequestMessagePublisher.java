package com.food.ordering.system.order.service.domain.ports.output.message.publisher.paymentApproval;

import com.food.ordering.system.order.service.domain.event.orderCancelledEvent;
import domain.event.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<orderCancelledEvent> {
}
