package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {
    private final RestaurantApprovalRequestHelper helper;

    public RestaurantApprovalRequestMessageListenerImpl(RestaurantApprovalRequestHelper helper) {
        this.helper = helper;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        helper.persistOrderApproval(restaurantApprovalRequest);
    }
}
