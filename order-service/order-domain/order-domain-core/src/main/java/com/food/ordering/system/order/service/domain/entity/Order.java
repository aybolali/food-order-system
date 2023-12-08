package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueObject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueObject.TrackingId;
import domain.entity.AggregateRoot;
import domain.valueObject.*;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerID customerID;
    private final RestaurantID restaurantID;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    //we will set them(below) during business logic after creating the order entity
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemID = 1;
        for(OrderItem orderItem : items){
            orderItem.intiializeOrderItem(super.getId(), new OrderItemId(itemID++));
        }
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    //from pending to paid status
    public void pay(){
        if (orderStatus != OrderStatus.PENDING)
            throw new OrderDomainException("not in correct state for pay operation");

        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if (orderStatus != OrderStatus.PAID)
            throw new OrderDomainException("not in correct state for approve operation");

        orderStatus = OrderStatus.APPROVED;
    }

    //from paid to cancelling (not approved)
    public void initCancel(List<String> failureMessages){
        if (orderStatus != OrderStatus.PAID)
            throw new OrderDomainException("not in correct state for initCancel operation");

        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages){
        if (!(orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.CANCELLING))
            throw new OrderDomainException("not in correct state for cancel operation");

        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null)
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());

        if(this.failureMessages == null){
            this.failureMessages = failureMessages;
        }
    }
    private void validateItemsPrice() {
        Money orderItemsTotal =  items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal))
            throw new OrderDomainException("total price: " + price.getAmount() +
                    " is not equal to Order items total: " + orderItemsTotal.getAmount());
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid())
            throw new OrderDomainException("order item price: " + orderItem.getPrice().getAmount() +
                    "is not valid for product " + orderItem.getProduct().getId().getValue());
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero())
            throw new OrderDomainException("Total price must be greater than 0.");
    }

    private void validateInitialOrder() {
        if(orderStatus != null || getId() != null)  //when creating first time - there are null id before creating a new order (kinda)
            throw new OrderDomainException("Order is not in correct state for initialization.");
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerID = builder.customerID;
        restaurantID = builder.restaurantID;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }
    public CustomerID getCustomerID() {
        return customerID;
    }

    public RestaurantID getRestaurantID() {
        return restaurantID;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerID customerID;
        private RestaurantID restaurantID;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }


        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }

        public Builder restaurantID(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        } //so firstly it will create a new Builder object calling Order class(for now, we do not have an Order object), then collecting variables with setters it will be a whole builder object with some characteristics.
        //then by build() method we create a new Order object by giving in argument Order(this) - in detail we're giving a Builder object with characteristics that we set all one moment before
        //na momente perekidyvaesh' ves' characteristics objecta na drugoi novyi object klassa by using this.
    }
}
