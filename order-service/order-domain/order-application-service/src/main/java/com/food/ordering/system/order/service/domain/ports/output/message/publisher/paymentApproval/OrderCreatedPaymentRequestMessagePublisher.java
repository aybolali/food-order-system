package com.food.ordering.system.order.service.domain.ports.output.message.publisher.paymentApproval;

import com.food.ordering.system.order.service.domain.event.orderCreatedEvent;
import domain.event.publisher.DomainEventPublisher;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<orderCreatedEvent> {

}
