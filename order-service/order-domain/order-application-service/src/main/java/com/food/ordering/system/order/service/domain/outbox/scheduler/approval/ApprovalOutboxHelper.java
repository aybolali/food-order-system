package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.module.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.module.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
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

@Slf4j
@Component
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository approvalOutboxRepository;
    private final ObjectMapper objectMapper;

    public ApprovalOutboxHelper(ApprovalOutboxRepository approvalOutboxRepository, ObjectMapper objectMapper) {
        this.approvalOutboxRepository = approvalOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId, sagaStatus);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage){
        OrderApprovalOutboxMessage response = approvalOutboxRepository.save(orderApprovalOutboxMessage);
        if(response == null){
            log.error("Could not save OrderApprovalOutboxMessage with outbox id: {}",
                    orderApprovalOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderApprovalOutboxMessage with outbox id: " +
                    orderApprovalOutboxMessage.getId());
        }
        log.info("OrderApprovalOutboxMessage saved with outbox id: {}", orderApprovalOutboxMessage.getId());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);
    }

    @Transactional
    public void saveApprovalOutboxMessage(OrderApprovalEventPayload payload,
                                          OrderStatus orderStatus, SagaStatus sagaStatus,
                                          OutboxStatus outboxStatus, UUID sagaId) {
        approvalOutboxRepository.save(OrderApprovalOutboxMessage.builder()
                        .id(UUID.randomUUID())
                        .sagaId(sagaId)
                        .createdAt(payload.getCreatedAt())
                        .type(ORDER_SAGA_NAME)
                        .payload(createPayload(payload))
                        .orderStatus(orderStatus)
                        .outboxStatus(outboxStatus)
                        .sagaStatus(sagaStatus)
                .build());
    }

    private String createPayload(OrderApprovalEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload); //json
        } catch (JsonProcessingException e){
            log.error("Could not create OrderApprovalEventPayload for order id: {}",
                    payload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderApprovalEventPayload");
        }
    }
}
