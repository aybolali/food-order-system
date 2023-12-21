package com.food.ordering.system.payment.service.domain.dto;

import domain.valueObject.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequest {
    private String id;
    private String customerId;
    private String sagaId;
    private String orderId;
    private BigDecimal price;
    private Instant createdAt;

    private PaymentOrderStatus paymentOrderStatus;

    public void setPaymentOrderStatus(PaymentOrderStatus paymentOrderStatus) { //just need for update
        this.paymentOrderStatus = paymentOrderStatus;
    }
}
