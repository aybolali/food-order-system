package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.PaymentDomainService;
import com.food.ordering.system.domain.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService paymentDomainService(){
        return new PaymentDomainServiceImpl();
    }
}