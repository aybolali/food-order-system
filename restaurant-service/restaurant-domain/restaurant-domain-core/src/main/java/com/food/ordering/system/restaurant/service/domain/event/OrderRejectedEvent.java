package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.domain.valueObject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent{

    public OrderRejectedEvent(OrderApproval orderApproval, RestaurantID restaurantId, List<String> failureMessages, ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
