package com.food.ordering.system.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
@Slf4j
public class KafkaMessageHelper {
    public <T> BiConsumer<SendResult<String, T>, Throwable> getKafkaCallback(String responseTopicName,
                                                                  T AvroModel, //RestaurantApprovalRequestAvroModel, PaymentRequestAvroModel - check history why this from previous one
                                                                  String orderId, String AvroModelName
                                                                                ) {

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
            } else {
                log.error("Error while sending " + AvroModelName +
                        " message {} to topic {}", AvroModel.toString(), responseTopicName, exception);
            }
        };
    }
}
