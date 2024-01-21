package com.food.ordering.system.order.service.dataAccess.outbox.restaurantapproval.repository;

import com.food.ordering.system.order.service.dataAccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import outbox.OutboxStatus;
import saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalOutboxJpaRepository extends JpaRepository<ApprovalOutboxEntity, UUID> {

    Optional<List<ApprovalOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                  OutboxStatus outboxStatus,
                                                                                  List<SagaStatus> sagaStatus);

    Optional<ApprovalOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type,
                                                                      UUID sagaId,
                                                                      List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);

}
