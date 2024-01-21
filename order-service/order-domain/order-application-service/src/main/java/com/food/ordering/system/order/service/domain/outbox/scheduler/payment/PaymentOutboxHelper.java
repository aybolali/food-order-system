package com.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.module.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.outbox.module.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.domain.valueObject.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import outbox.OutboxStatus;
import saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static saga.order.SagaConstants.ORDER_SAGA_NAME;

@Component
@Slf4j
public class PaymentOutboxHelper {
    private final PaymentOutboxRepository repository;
    private final ObjectMapper objectMapper;

    public PaymentOutboxHelper(PaymentOutboxRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true) //for only getting a data
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses){
        return repository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
                outboxStatus,sagaStatuses);
    }

    @Transactional(readOnly = true) //for only getting a data
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId, SagaStatus... sagaStatuses){
        return repository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME,
                sagaId, sagaStatuses);
    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = repository.save(orderPaymentOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id: " +
                    orderPaymentOutboxMessage.getId());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId());
    }

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload orderPaymentEventPayload,
                                         OrderStatus orderStatus,
                                         SagaStatus sagaStatus,
                                         OutboxStatus outboxStatus,
                                         UUID sagaId){
        save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderPaymentEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                      SagaStatus... sagaStatuses){
        repository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload); //json
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderPaymentEventPayload object for order id: {}",
                    orderPaymentEventPayload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderPaymentEventPayload object for order id: " +
                    orderPaymentEventPayload.getOrderId(), e);
        }
    }
}
