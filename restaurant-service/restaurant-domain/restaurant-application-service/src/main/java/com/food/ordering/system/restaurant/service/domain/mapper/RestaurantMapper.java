package com.food.ordering.system.restaurant.service.domain.mapper;

import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.domain.valueObject.OrderStatus;
import com.food.ordering.system.domain.valueObject.RestaurantID;
import org.springframework.stereotype.Component;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public Restaurant
    restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest //po idei from request to entity
                                                                     restaurantApprovalRequest){
        return Restaurant.builder()
                .id(new RestaurantID(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .id(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .id(product.getId())
                                                .quantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .status(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }

    public OrderEventPayload
    orderApprovalEventToOrderEventPayload(OrderApprovalEvent orderApprovalEvent) {
        return OrderEventPayload.builder()
                .orderId(orderApprovalEvent.getOrderApproval().getOrderId().toString())
                .restaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
                .createdAt(orderApprovalEvent.getCreatedAt())
                .orderApprovalStatus(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus().name())
                .failureMessages(orderApprovalEvent.getFailureMessages())
                .build();

    }
}
