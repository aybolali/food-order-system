package com.food.ordering.system.order.service.dataAccess.restaurant.repository;

import com.food.ordering.system.order.service.dataAccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataAccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds); //will be converted to SQL query
}
