package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.orderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.paymentApproval.OrderCancelledPaymentRequestMessagePublisher;
import domain.event.EmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import saga.SagaStep;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, orderCancelledEvent> {
    private final OrderSagaHelper helper;
    private final OrderDomainService service;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

    public OrderApprovalSaga(OrderSagaHelper helper, OrderDomainService service, OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher) {
        this.helper = helper;
        this.service = service;
        this.orderCancelledPaymentRequestMessagePublisher = orderCancelledPaymentRequestMessagePublisher;
    }

    //for Approval there will be no next step in the saga flow - answer for why empty event to process
    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse response) {
        log.info("Approving order with id: {}", response.getOrderId());
        Order order = helper.findOrder(response.getOrderId());
        service.approveOrder(order);
        helper.SaveOrder(order);
        log.info("Order with id: {} is approved", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    @Transactional
    public orderCancelledEvent rollback(RestaurantApprovalResponse response) {
        log.info("Cancelling order with id: {}", response.getOrderId());
        Order order = helper.findOrder(response.getOrderId());
        orderCancelledEvent orderCancelledEvent = service.cancelOrderPayment(order,
                response.getFailureMessages(),
                orderCancelledPaymentRequestMessagePublisher
        );
        helper.SaveOrder(order);
        log.info("Order with id: {} is cancelling", order.getId().getValue());
        return orderCancelledEvent;
    }
}
