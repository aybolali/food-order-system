package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.OrderDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //spring-managed configuration Bean
public class BeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService(){ //register it as a spring bean - because domain core part does not have any dependency -> so it's not marked as a spring bean before registering
        return new OrderDomainServiceImpl();
    }
}
