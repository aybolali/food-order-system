package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.CustomerID;

public class Customer extends AggregateRoot <CustomerID> {

    public Customer(){

    }
    public Customer(CustomerID customerID) {
        super.setId(customerID);
    }
}
