package com.food.ordering.system.domain.valueObject;

import domain.valueObject.BaseId;

import java.util.UUID;

public class PaymentID extends BaseId<UUID> {
    public PaymentID(UUID value){
        super(value);
    }
}
