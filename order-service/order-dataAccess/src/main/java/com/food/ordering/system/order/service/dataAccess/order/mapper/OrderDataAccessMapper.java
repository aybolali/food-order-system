package com.food.ordering.system.order.service.dataAccess.order.mapper;

import com.food.ordering.system.order.service.dataAccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataAccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataAccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueObject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueObject.TrackingId;
import domain.valueObject.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper  {
    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .restaurantId(order.getRestaurantID().getValue())
                .customerId(order.getCustomerID().getValue())
                .trackingId(order.getTrackingId().getValue())
                .price(order.getPrice().getAmount())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(",", order.getFailureMessages()) : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);//object order null bolmas ushin
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));//object order null bolmas ushin
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity){
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .restaurantID(new RestaurantID(orderEntity.getRestaurantId()))
                .customerID(new CustomerID(orderEntity.getCustomerId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .price(new Money(orderEntity.getPrice()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .items(orderItemEntitiesToOrderItem(orderEntity.getItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                .split(","))))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItem(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductID(orderItemEntity.getProductId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build()
                ).collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(
                address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }



    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build()
                ).collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }
}
