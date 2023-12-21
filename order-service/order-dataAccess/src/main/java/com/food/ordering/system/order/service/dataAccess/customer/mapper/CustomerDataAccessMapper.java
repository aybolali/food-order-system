package com.food.ordering.system.order.service.dataAccess.customer.mapper;

import com.food.ordering.system.order.service.dataAccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import domain.valueObject.CustomerID;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity){
        return new Customer(new CustomerID(customerEntity.getId()));
    }

    public CustomerEntity customerToCustomerEntity(Customer customer){
        return CustomerEntity.builder().id(customer.getId().getValue()).build();
    }
}
