package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.event.PaymentEvents;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {
    private final PaymentRequestHelper paymentRequestHelper;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest);
    }
    /*private final PaymentRequestHelper paymentRequestHelper;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;

    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvents paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvents paymentEvent= paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(PaymentEvents paymentEvent) {
        log.info("Publishing payment event with payment id: {} and order id: {}",
                paymentEvent.getPayment().getId(),
                paymentEvent.getPayment().getOrderId().getValue());
        paymentEvent.fire();
    }*/
}
