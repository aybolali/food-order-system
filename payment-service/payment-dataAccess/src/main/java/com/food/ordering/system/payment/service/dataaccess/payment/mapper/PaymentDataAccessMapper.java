package com.food.ordering.system.payment.service.dataaccess.payment.mapper;


import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.valueObject.PaymentID;
import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity;

import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {

    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerID().getValue())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .id(new PaymentID(paymentEntity.getId()))
                .customerID(new CustomerID(paymentEntity.getCustomerId()))
                .orderId(new OrderId(paymentEntity.getOrderId()))
                .price(new Money(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }

}
