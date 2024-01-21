package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.restaurant.service.domain.valueObject.OrderApprovalId;
import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueObject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.domain.valueObject.RestaurantID;

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final RestaurantID restaurantID;
    private final OrderId orderId;

    private final OrderApprovalStatus orderApprovalStatus;

    public RestaurantID getRestaurantID() {
        return restaurantID;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getOrderApprovalStatus() {
        return orderApprovalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    private OrderApproval(Builder builder) {
        super.setId(builder.id);
        restaurantID = builder.restaurantID;
        orderId = builder.orderId;
        orderApprovalStatus = builder.orderApprovalStatus;
    }

    public static final class Builder {
        private OrderApprovalId id;
        private RestaurantID restaurantID;
        private OrderId orderId;

        private OrderApprovalStatus orderApprovalStatus;

        private Builder() {
        }

        public Builder orderApprovalStatus(OrderApprovalStatus val) {
            orderApprovalStatus = val;
            return this;
        }

        public Builder id(OrderApprovalId val) {
            id = val;
            return this;
        }

        public Builder restaurantID(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
