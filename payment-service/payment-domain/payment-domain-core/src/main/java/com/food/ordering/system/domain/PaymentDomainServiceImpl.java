package com.food.ordering.system.domain;

import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.entity.CreditHistory;
import com.food.ordering.system.domain.entity.Payment;
import com.food.ordering.system.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.domain.event.PaymentEvents;
import com.food.ordering.system.domain.event.PaymentFailedEvent;
import com.food.ordering.system.domain.valueObject.CreditHistoryId;
import com.food.ordering.system.domain.valueObject.TransactionType;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService{

    @Override
    public PaymentEvents validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                                    List<CreditHistory> creditHistories,
                                                    List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry); //sdacha if higher than 0
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditHistories, creditEntry, failureMessages);
        if(failureMessages.isEmpty()){
            log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    @Override
    public PaymentEvents validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                                  List<CreditHistory> creditHistories, List<String> failureMessages
                                                  ) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);  //return
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);
        if(failureMessages.isEmpty()){
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if(payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id: {} does not have enough money to pay it!",
                    payment.getCustomerID().getValue());
            failureMessages.add("Customer with id= " + payment.getCustomerID().getValue()
            + " does not have enough credit!");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .customerID(payment.getCustomerID())
                        .amount(payment.getPrice())
                        .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(List<CreditHistory> creditHistories,
                                       CreditEntry creditEntry,
                                       List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if(totalDebitHistory.isGreaterThan(totalCreditHistory)){ //totalCreditHistory - senin tolegen akshanyn summasy <-> total debit for this +- schet
            //from side of customer debit kobirek bolganda restaurant ubytokka ketedi - total credit history eto to skolko ty potratish'
            log.error("Customer with id: {} does not have enough credit according to credit history", creditEntry.getCustomerID());
            failureMessages.add("Customer with id:"+ creditEntry.getCustomerID() +
                    " does not have enough credit according to credit history");
        }
        if(!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.substract(totalDebitHistory))){ //сдача after subtractCreditEntry - okonchatel'nyi sonynda sende kansha kaldy sdacha
            // a tak v selom CreditEntry is the amount of money in customer's account.
            log.error("Credit history total is not equal to current credit for customer id: {}",
                    creditEntry.getCustomerID());

            failureMessages.add("Credit history total is not equal to current credit for customer id: " +
                    creditEntry.getCustomerID());
        }
    }

    private static Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

}

