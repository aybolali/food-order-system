package com.food.ordering.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.food.ordering.system.order.service.dataAccess", "common.dataAccess" })
@EntityScan(basePackages = { "com.food.ordering.system.order.service.dataAccess", "common.dataAccess"}) //required for a multi-module application
@SpringBootApplication(scanBasePackages = "com.food.ordering.system") //to all modules
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
