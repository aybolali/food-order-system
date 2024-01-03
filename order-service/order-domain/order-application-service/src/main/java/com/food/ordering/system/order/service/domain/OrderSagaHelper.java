package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import domain.valueObject.OrderId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {
    private final OrderRepository repository;

    public OrderSagaHelper(OrderRepository repository) {
        this.repository = repository;
    }

    Order findOrder(String orderId){
        Optional<Order> response = repository.findById(new OrderId(UUID.fromString(orderId)));
        if (response.isEmpty()){
            log.error("order with id: {} could not be found", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found");
        }
        return response.get();
    }
    void SaveOrder(Order order){
        repository.save(order);
    }
}
