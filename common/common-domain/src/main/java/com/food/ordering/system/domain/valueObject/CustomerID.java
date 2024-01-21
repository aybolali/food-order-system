package com.food.ordering.system.domain.valueObject;

import java.util.UUID;

public class CustomerID extends BaseId<UUID>{

    public CustomerID(UUID value) {
        super(value);
    }
}

