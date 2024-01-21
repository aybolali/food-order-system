package com.food.ordering.system.order.service.dataAccess.restaurant.mapper;

import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.domain.valueObject.Money;
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

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities){
        RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> productsList = restaurantEntities.stream().map(
                entity ->
                        new Product(new ProductID(entity.getProductId()), entity.getProductName(),
                                new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantID(restaurantEntity.getRestaurantId()))
                .products(productsList)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant){
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());//list of product ID values
    }

}
