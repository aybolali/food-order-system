package com.food.ordering.system.domain.entity;

import com.food.ordering.system.domain.valueObject.CreditHistoryId;
import com.food.ordering.system.domain.valueObject.TransactionType;
import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;

public class CreditHistory extends BaseEntity<CreditHistoryId> {//not aggregate so they can act independently from a payment
    private final CustomerID customerID;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditHistoryId);
        customerID = builder.customerID;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public CustomerID getCustomerID() {
        return customerID;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        public CreditHistoryId creditHistoryId;
        private CustomerID customerID;
        private Money amount;

        private TransactionType transactionType;

        private Builder() {
        }

        public Builder creditHistoryId(CreditHistoryId val){
            creditHistoryId = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }
        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
