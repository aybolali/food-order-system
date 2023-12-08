package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.valueObject.TrackingId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OrderRepository {
    Order save (Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId); //optional because I may or may not find an order with that tracking id

}
