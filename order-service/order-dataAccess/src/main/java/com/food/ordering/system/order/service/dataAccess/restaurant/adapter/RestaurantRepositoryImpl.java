package com.food.ordering.system.order.service.dataAccess.restaurant.adapter;

import com.food.ordering.system.order.service.dataAccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;
import common.dataAccess.restaurant.entity.RestaurantEntity;
import common.dataAccess.restaurant.repository.RestaurantJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final RestaurantJpaRepository JpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository jpaRepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        JpaRepository = jpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntities =
                JpaRepository.findByRestaurantIdAndProductIdIn(
                        restaurant.getId().getValue(),
                        restaurantProducts);

        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);

        /*return JpaRepository.findByRestaurantIdAndProductIdIn(
                restaurant.getId().getValue(),
                restaurantProducts).map(restaurantDataAccessMapper::restaurantEntityToRestaurant);*/
    }
}
