package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper;


import com.food.ordering.system.restaurant.service.domain.valueObject.OrderApprovalId;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;

import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.domain.valueObject.ProductID;
import com.food.ordering.system.domain.valueObject.RestaurantID;
import org.springframework.stereotype.Component;
import com.food.ordering.system.common.dataAccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.common.dataAccess.restaurant.exception.RestaurantDataAccessException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("No restaurants found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                        Product.builder()
                                .id(new ProductID(entity.getProductId()))
                                .name(entity.getProductName())
                                .money(new Money(entity.getProductPrice()))
                                .available(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .id(new RestaurantID(restaurantEntity.getRestaurantId()))
                .orderDetail(OrderDetail.builder()
                        .products(restaurantProducts)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantID().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getOrderApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .id(new OrderApprovalId(orderApprovalEntity.getId()))
                .restaurantID(new RestaurantID(orderApprovalEntity.getRestaurantId()))
                .orderId(new OrderId(orderApprovalEntity.getOrderId()))
                .orderApprovalStatus(orderApprovalEntity.getStatus())
                .build();
    }

}
