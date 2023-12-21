package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.entity.Payment;
import domain.valueObject.CustomerID;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);

}
