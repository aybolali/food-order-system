package com.food.ordering.system.domain;

import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.entity.CreditHistory;
import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.event.PaymentEvents;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvents validateAndInitiatePayment(Payment payment,
                                             CreditEntry creditEntry,
                                             List<CreditHistory>creditHistories,
                                             List<String> failureMessages);

    PaymentEvents validateAndCancelPayment(Payment payment,
                                           CreditEntry creditEntry,
                                           List<CreditHistory>creditHistories,
                                           List<String> failureMessages);

}
