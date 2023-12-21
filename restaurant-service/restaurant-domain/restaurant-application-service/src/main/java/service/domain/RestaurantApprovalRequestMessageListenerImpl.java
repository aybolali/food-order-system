package service.domain;

import domain.event.OrderApprovalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import service.domain.dto.RestaurantApprovalRequest;
import service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;

@Service
@Slf4j
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {
    private final RestaurantApprovalRequestHelper helper;

    public RestaurantApprovalRequestMessageListenerImpl(RestaurantApprovalRequestHelper helper) {
        this.helper = helper;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        OrderApprovalEvent event = helper.persistOrderApproval(restaurantApprovalRequest);
        event.fire();
    }
}
