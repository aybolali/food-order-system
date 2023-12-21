package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import domain.valueObject.CustomerID;
import domain.valueObject.Money;
import domain.valueObject.OrderId;
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
}
