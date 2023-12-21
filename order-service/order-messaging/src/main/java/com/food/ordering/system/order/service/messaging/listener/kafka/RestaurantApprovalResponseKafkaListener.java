package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurauntApproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;

    public RestaurantApprovalResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper, RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}") // annotates method that mentioning it is a listener consumer method to kafka
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages, //extract the main contents(entire data) of the message
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys, //header is the metadata
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        //message has 2 parts - entire data(@payload), metadata(@header)
        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(message -> {
            if(message.getOrderApprovalStatus()== OrderApprovalStatus.APPROVED){
                log.info("Processing approved for order id: {}", message.getOrderId());
                restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper
                        .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message));
            } else if(message.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED){
                log.info("Processing rejected for order id: {}, with failure messages: {}",
                        message.getOrderId(),
                        String.join(",", message.getFailureMessages())
                );
                restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper
                        .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message));
            }
        });
    }
}
