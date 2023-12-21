package domain.entity;

import domain.valueObject.Money;
import domain.valueObject.OrderId;
import domain.valueObject.OrderStatus;

import java.util.List;

public class OrderDetail extends BaseEntity<OrderId>{
    private OrderStatus status;
    private Money totalAmount;
    private final List<Product> products;

    private OrderDetail(Builder builder) {
        setId(builder.orderId);
        status = builder.status;
        totalAmount = builder.totalAmount;
        products = builder.products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }
    public static final class Builder {
        private OrderId orderId;
        private OrderStatus status;
        private Money totalAmount;

        private List<Product> products;

        private Builder() {
        }

        public Builder id(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder status(OrderStatus val) {
            status = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
