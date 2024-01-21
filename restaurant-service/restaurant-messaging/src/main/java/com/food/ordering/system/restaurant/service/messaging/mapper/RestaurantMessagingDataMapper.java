package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.domain.valueObject.ProductID;
import com.food.ordering.system.domain.valueObject.RestaurantOrderStatus;
import org.springframework.stereotype.Component;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessagingDataMapper {
   /* public RestaurantApprovalResponseAvroModel
    orderApprovedEventToRestaurantApprovalResponseAvroModel(OrderApprovedEvent orderApprovedEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.fromString(UUID.randomUUID().toString()))
                .setSagaId(UUID.fromString(""))
                .setOrderId(orderApprovedEvent.getOrderApproval().getOrderId().getValue())
                .setRestaurantId(orderApprovedEvent.getRestaurantId().getValue())
                .setCreatedAt(orderApprovedEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovedEvent.
                        getOrderApproval().getOrderApprovalStatus().name()))
                .setFailureMessages(orderApprovedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(""))
                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue())
                .setRestaurantId(orderRejectedEvent.getRestaurantId().getValue())
                .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderRejectedEvent.
                        getOrderApproval().getOrderApprovalStatus().name()))
                .setFailureMessages(orderRejectedEvent.getFailureMessages())
                .build();
    }
*/
    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel
                                                                   restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId().toString())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId().toString())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId().toString())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts()
                        .stream().map(avroModel ->
                                Product.builder()
                                        .id(new ProductID(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

    public RestaurantApprovalResponseAvroModel orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload orderEventPayload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setOrderId(UUID.fromString(orderEventPayload.getOrderId()))
                .setRestaurantId(UUID.fromString(orderEventPayload.getRestaurantId()))
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
