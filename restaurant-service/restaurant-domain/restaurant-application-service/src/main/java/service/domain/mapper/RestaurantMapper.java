package service.domain.mapper;

import domain.entity.OrderDetail;
import domain.entity.Product;
import domain.entity.Restaurant;
import domain.valueObject.Money;
import domain.valueObject.OrderId;
import domain.valueObject.OrderStatus;
import domain.valueObject.RestaurantID;
import org.springframework.stereotype.Component;
import service.domain.dto.RestaurantApprovalRequest;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest //po idei from request to entity
                                                                     restaurantApprovalRequest){
        return Restaurant.builder()
                .id(new RestaurantID(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .id(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .id(product.getId())
                                                .quantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .status(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }
}
