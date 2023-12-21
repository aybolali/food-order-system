package domain.entity;

import domain.valueObject.*;

import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantID>{
    private OrderApproval orderApproval;
    private boolean active;
    private final OrderDetail orderDetail;
    private Restaurant(Builder builder) {
        setId(builder.id);
        orderApproval = builder.orderApprovalID;
        active = builder.active;
        orderDetail = builder.orderDetail;
    }

    public void validateOrder(List<String> failureMessages){
        if(orderDetail.getStatus() != OrderStatus.PAID){
            failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
        }
        Money totalAmount = orderDetail.getProducts().stream().map(product -> {
            if (!product.isAvailable())
                failureMessages.add("Product with id: " + product.getId().getValue()
                + " is not available");
            return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO, Money::add); //starting from 0 to adding all product sum

        if(!totalAmount.equals(orderDetail.getTotalAmount()))
            failureMessages.add("Price total is not correct for order: " + orderDetail.getId());
    }

    public void constructOrderApproval(OrderApprovalStatus status){
        this.orderApproval = OrderApproval.builder()
                .id(new OrderApprovalId(UUID.randomUUID()))
                .orderId(this.orderApproval.getOrderId())
                .restaurantID(this.getId())
                .orderApprovalStatus(orderApproval.getOrderApprovalStatus())
                .build();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }


    public static final class Builder {
        private RestaurantID id;
        private OrderApproval orderApprovalID;
        private boolean active;

        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder id(RestaurantID val) {
            id = val;
            return this;
        }

        public Builder orderApprovalID(OrderApproval val) {
            orderApprovalID = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
