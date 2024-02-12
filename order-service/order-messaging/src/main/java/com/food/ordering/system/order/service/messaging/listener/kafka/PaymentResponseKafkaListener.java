package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.paymentApproval.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel>{
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final PaymentResponseMessageListener paymentResponseMessageListener;

    public PaymentResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                        PaymentResponseMessageListener paymentResponseMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.paymentResponseMessageListener = paymentResponseMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {
                    try {
                        if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()){
                            log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                            paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                                    .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                        } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus() ||
                                PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()){
                            log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                            paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper
                                    .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                        }
                    } catch (OptimisticLockingFailureException e) {
                        // means another thread finished the work, do not throw error to prevent reading the data from kafka again! (no duplicate)
                        log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                                paymentResponseAvroModel.getOrderId());
                    } catch (OrderNotFoundException e) {
                        log.error("No order found for order id: {}", paymentResponseAvroModel.getOrderId());
                    }
                }
        );
    }
}
