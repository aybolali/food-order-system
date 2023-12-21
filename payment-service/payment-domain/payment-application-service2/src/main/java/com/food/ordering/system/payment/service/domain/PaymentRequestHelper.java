package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.PaymentDomainService;
import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.entity.CreditHistory;
import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.domain.event.PaymentEvents;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import domain.event.publisher.DomainEventPublisher;
import domain.valueObject.CustomerID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService, PaymentDataMapper paymentDataMapper, PaymentRepository paymentRepository, CreditEntryRepository creditEntryRepository, CreditHistoryRepository creditHistoryRepository, DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher, PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher, PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedEventDomainEventPublisher = (PaymentCompletedMessagePublisher) paymentCompletedEventDomainEventPublisher;
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
        this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
    }

    @Transactional
    public PaymentEvents persistPayment(PaymentRequest paymentRequest){
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getHistories(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvents paymentEvent = paymentDomainService.validateAndInitiatePayment(
                payment, creditEntry, creditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher,
                paymentFailedEventDomainEventPublisher);
        persistDboObjects(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }

    private void persistDboObjects(Payment payment,
                                   CreditEntry creditEntry,
                                   List<CreditHistory> creditHistories,
                                   List<String> failureMessages) {
        paymentRepository.save(payment);
        if(failureMessages.isEmpty()){
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    @Transactional
    public PaymentEvents persistCancelPayment(PaymentRequest paymentRequest){
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if(paymentResponse.isEmpty()){
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " +
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getHistories(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvents paymentEvent = paymentDomainService.validateAndCancelPayment(
                payment, creditEntry, creditHistories, failureMessages,
                paymentCancelledEventDomainEventPublisher,
                paymentFailedEventDomainEventPublisher);
        persistDboObjects(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }
    private CreditEntry getCreditEntry(CustomerID customerID) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerID);
        if(creditEntry.isEmpty()){
            log.error("Could not find credit entry for customer: {}", customerID.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer:" + customerID.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getHistories(CustomerID customerID) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerID);
        if(creditHistories.isEmpty()){
            log.error("Could not find credit history for customer: {}", customerID.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " + customerID.getValue());
        }
        return creditHistories.get();
    }
}