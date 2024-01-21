package com.food.ordering.system.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import outbox.OutboxStatus;

import java.util.function.BiConsumer;

@Component
@Slf4j
public class KafkaMessageHelper {
    private final ObjectMapper objectMapper;

    public KafkaMessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getOrderEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e){
            log.error("Could not read {}  object!", outputType.getName() ,e);
            throw new OrderDomainException("Could not read OrderPaymentEventPayload object!", e);
        }
    }
    public <T, U> BiConsumer<SendResult<String, T>, Throwable> getKafkaCallback(String responseTopicName,
                                                                  T AvroModel, //RestaurantApprovalRequestAvroModel, PaymentRequestAvroModel - check history why this from previous one
                                                                  U outboxMessage, BiConsumer<U, OutboxStatus> outboxCallback,
                                                                                String orderId, String AvroModelName) {

        return (successResult, exception) -> {
            if(exception == null){
                RecordMetadata metadata = successResult.getRecordMetadata();
                log.info("Received successful response from kafka for order id: {} " +
                                " Topic: {} Partition: {} Offset: {} Timestamp:{}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());
                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            } else {
                log.error("Error while sending " + AvroModelName +
                        " message {} and outbox type {} to topic {}",
                        AvroModel.toString(), outboxMessage.getClass().getName(), responseTopicName, exception);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }
        };
    }
}
