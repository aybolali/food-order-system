package com.food.ordering.system.domain;

import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.entity.CreditHistory;
import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.domain.event.PaymentEvents;
import com.food.ordering.system.domain.event.PaymentFailedEvent;
import domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvents validateAndInitiatePayment(Payment payment,
                                             CreditEntry creditEntry,
                                             List<CreditHistory>creditHistories,
                                             List<String> failureMessages,
                                             DomainEventPublisher<PaymentCompletedEvent>
                                                     paymentCompletedEventDomainEventPublisher,
                                             DomainEventPublisher<PaymentFailedEvent>
                                                     paymentFailedEventDomainEventPublisher);

    PaymentEvents validateAndCancelPayment(Payment payment,
                                           CreditEntry creditEntry,
                                           List<CreditHistory>creditHistories,
                                           List<String> failureMessages,
                                           DomainEventPublisher<PaymentCancelledEvent>
                                                   paymentCancelledEventDomainEventPublisher,
                                           DomainEventPublisher<PaymentFailedEvent>
                                                   paymentFailedEventDomainEventPublisher);

}
