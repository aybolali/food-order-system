package com.food.ordering.system.order.service.domain.entity;

import domain.entity.AggregateRoot;
import domain.valueObject.CustomerID;

public class Customer extends AggregateRoot <CustomerID> {

    public Customer(){

    }
    public Customer(CustomerID customerID) {
        super.setId(customerID);
    }
}
