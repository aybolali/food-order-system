package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.ProductID;

public class Product extends BaseEntity<ProductID> {
    private String name;
    private Money price;

    public Product(ProductID productID, String name, Money price) {
        super.setId(productID);
        this.name = name;
        this.price = price;
    }

    public Product(ProductID productID) {
        super.setId(productID);
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price){
        this.name = name;
        this.price = price;
    }
    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
