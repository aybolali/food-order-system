package com.food.ordering.system.order.service.domain.entity;

import domain.entity.AggregateRoot;
import domain.valueObject.RestaurantID;
import lombok.Builder;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantID> {
    private RestaurantID restaurantID;
    private final List<Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantID);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static final class Builder {
        private RestaurantID restaurantID;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder RestaurantId(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
