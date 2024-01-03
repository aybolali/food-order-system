package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.orderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantApproval.OrderPaidRestaurantRequestMessagePublisher;
import domain.event.EmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import saga.SagaStep;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, orderPaidEvent, EmptyEvent> {
    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper helper;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    public OrderPaymentSaga(OrderDomainService orderDomainService,
                            OrderSagaHelper helper,
                            OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.helper = helper;
        this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
    }

    @Override
    @Transactional //database transaction
    public orderPaidEvent process(PaymentResponse data) {
        log.info("Completing payment for order with id: " + data.getOrderId());
        Order order = helper.findOrder(data.getOrderId());
        orderPaidEvent paidEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
        helper.SaveOrder(order);
        log.info("Order with id: {} is paid", order.getId().getValue());
        return paidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse data) {
        log.info("Cancelled order with id: {} ", data.getOrderId());
        Order order = helper.findOrder(data.getOrderId());
        orderDomainService.cancelOrder(order, data.getFailureMessages());
        helper.SaveOrder(order);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

}
