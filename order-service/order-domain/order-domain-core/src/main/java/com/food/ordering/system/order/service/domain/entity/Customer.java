package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.CustomerID;

public class Customer extends AggregateRoot<CustomerID> {
    private String username;
    private String firstname;
    private String lastname;

    public Customer(CustomerID customerID, String username, String firstname, String lastname){
        super.setId(customerID);
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Customer(){

    }
    public Customer(CustomerID customerID) {
        super.setId(customerID);
    }
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }
}
