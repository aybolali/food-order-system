package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.orderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.domain.valueObject.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import outbox.OutboxStatus;

import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {
    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, PaymentOutboxHelper paymentOutboxHelper, OrderSagaHelper orderSagaHelper) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.orderSagaHelper = orderSagaHelper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        orderCreatedEvent orderCreatedEvent =  orderCreateHelper.persistOrder(createOrderCommand);
        log.info("new order is created: {}", orderCreatedEvent.getOrder().getId());
        CreateOrderResponse response = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order Created Successfully");

        OrderStatus orderStatus = orderCreatedEvent.getOrder().getOrderStatus();
        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderStatus,
                orderSagaHelper.orderStatusToSagaStatus(orderStatus),
                OutboxStatus.STARTED,
                UUID.randomUUID()
                );
        log.info("Returning CreateOrderResponse with order id: {}", orderCreatedEvent.getOrder().getId());
        return response;
    }
}
