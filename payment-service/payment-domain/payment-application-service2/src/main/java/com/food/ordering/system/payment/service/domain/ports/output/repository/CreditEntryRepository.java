package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.domain.entity.CreditEntry;
import domain.valueObject.CustomerID;

import java.util.Optional;

public interface CreditEntryRepository {
    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerID customerID);
}
