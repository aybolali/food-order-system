package com.food.ordering.system.payment.service.dataaccess.creditentry.mapper;

import com.food.ordering.system.domain.entity.CreditEntry;
import com.food.ordering.system.domain.valueObject.CreditEntryId;
import com.food.ordering.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .id(new CreditEntryId(creditEntryEntity.getId()))
                .customerID(new CustomerID(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerID().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
    }

}
