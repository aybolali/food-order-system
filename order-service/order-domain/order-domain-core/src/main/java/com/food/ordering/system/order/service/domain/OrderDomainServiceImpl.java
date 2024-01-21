package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.orderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.orderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.orderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {
    private static final String UTC = "UTC";
    @Override
    public orderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new orderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public orderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("order payment is cancelling for order id: {}", order.getId().getValue());
        return new orderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public orderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("order with id: {} is paid ", order.getId().getValue());
        return new orderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("order with id: {} is cancelled", order.getId().getValue());

    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
                    Product currentProductFromOrderItems = orderItem.getProduct();
                    if(currentProductFromOrderItems.equals(restaurantProduct)){ //find the corresponding product from restaurant to order item from Order items - like if I put a maklube in order items (zakaz) and in restaurant there is a maklube in their product lists - then updateWithConfirmedNameAndPrice
                        currentProductFromOrderItems.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                                restaurantProduct.getPrice());
                    }
                }
        ));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive())
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + "" +
                    "currently is not active.");
    }
}
