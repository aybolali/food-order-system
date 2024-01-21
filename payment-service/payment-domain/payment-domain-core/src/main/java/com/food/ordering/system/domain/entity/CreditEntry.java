package com.food.ordering.system.domain.entity;

import com.food.ordering.system.domain.valueObject.CreditEntryId;
import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;

public class CreditEntry extends BaseEntity<CreditEntryId> {
    private final CustomerID customerID;
    private Money totalCreditAmount;

    public void addCreditAmount(Money amount){
        totalCreditAmount = totalCreditAmount.add(amount);
    }

    public void subtractAmount(Money amount){
        totalCreditAmount = totalCreditAmount.substract(amount);
    }
    private CreditEntry(Builder builder) {
        super.setId(builder.creditEntryId);
        customerID = builder.customerID;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerID getCustomerID() {
        return customerID;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerID customerID;
        private Money totalCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
