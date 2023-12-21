package com.food.ordering.system.order.service.dataAccess.restaurant.mapper;

import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import domain.valueObject.Money;
import domain.valueObject.ProductID;
import domain.valueObject.RestaurantID;
import org.springframework.stereotype.Component;
import common.dataAccess.restaurant.entity.RestaurantEntity;
import common.dataAccess.restaurant.exception.RestaurantDataAccessException;

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
                .RestaurantId(new RestaurantID(restaurantEntity.getRestaurantId()))
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
