package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.event.PaymentEvents;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerID(new CustomerID(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money((paymentRequest.getPrice())))
                .build();
    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvents paymentEvent) {
        return OrderEventPayload.builder()
                .paymentId(paymentEvent.getPayment().getId().getValue().toString())
                .customerId(paymentEvent.getPayment().getCustomerID().getValue().toString())
                .orderId(paymentEvent.getPayment().getOrderId().getValue().toString())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentStatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
