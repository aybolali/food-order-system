package com.food.ordering.system.domain.valueObject;

import domain.valueObject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID value) {
        super(value);
    }
}
