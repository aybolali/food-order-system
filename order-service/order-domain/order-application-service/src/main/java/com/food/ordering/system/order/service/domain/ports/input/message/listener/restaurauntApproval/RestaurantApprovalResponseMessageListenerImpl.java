package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurauntApproval;

import com.food.ordering.system.order.service.domain.OrderApprovalSaga;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.event.orderCancelledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    private final OrderApprovalSaga approvalSaga;

    public RestaurantApprovalResponseMessageListenerImpl(OrderApprovalSaga approvalSaga) {
        this.approvalSaga = approvalSaga;
    }

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
        approvalSaga.process(restaurantApprovalResponse);
        log.info("Order is approved for order id: {}", restaurantApprovalResponse.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderCancelledEvent orderCancelledEvent = approvalSaga.rollback(restaurantApprovalResponse);
        log.info("Publishing order cancelled event for order id: {} with failure messages: {}",
                restaurantApprovalResponse.getOrderId(),
                String.join(" ", restaurantApprovalResponse.getFailureMessages()));
        orderCancelledEvent.fire();
    }
}

