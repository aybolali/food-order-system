package com.food.ordering.system.order.service;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.orderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.orderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.orderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    orderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    orderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    orderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    void cancelOrder(Order order, List<String> failureMessages);
}
