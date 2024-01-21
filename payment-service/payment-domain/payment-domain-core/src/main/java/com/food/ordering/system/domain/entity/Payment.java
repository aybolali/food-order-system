package com.food.ordering.system.domain.entity;

import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.domain.valueObject.CustomerID;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.domain.valueObject.PaymentStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class Payment extends AggregateRoot<PaymentID> {
    private final CustomerID customerID;
    private final OrderId orderId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    private Payment(Builder builder) {
        super.setId(builder.paymentID);
        customerID = builder.customerID;
        orderId = builder.orderId;
        price = builder.price;
        paymentStatus = builder.paymentStatus;
        createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }
    public void initializePayment(){
        super.setId(new PaymentID(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void validatePayment(List<String> failureMessages){
        if(price == null || !price.isGreaterThanZero()){
            failureMessages.add("Total price should be greater than 0!");
        }
    }

    public void updateStatus(PaymentStatus status){
        this.paymentStatus = status;
    }


    public CustomerID getCustomerID() {
        return customerID;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Money getPrice() {
        return price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public static final class Builder {
        private PaymentID paymentID;
        private CustomerID customerID;
        private OrderId orderId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder id(PaymentID val) {
            paymentID = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
