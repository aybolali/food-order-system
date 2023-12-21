package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.domain.entity.CreditHistory;
import domain.valueObject.CustomerID;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerID customerID);
}
