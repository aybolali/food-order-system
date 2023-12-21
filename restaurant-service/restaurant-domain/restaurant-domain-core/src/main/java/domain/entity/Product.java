package domain.entity;

import domain.valueObject.Money;
import domain.valueObject.ProductID;
//more detailed
public class Product extends BaseEntity<ProductID>{
    private String name;
    private Money price;
    private final int quantity;
    private boolean available;

    public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available){
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Product(Builder builder) {
        super.setId(builder.id);
        name = builder.name;
        price = builder.money;
        quantity = builder.quantity;
        available = builder.available;
    }
    public static final class Builder {
        private ProductID id;
        private String name;
        private Money money;
        private int quantity;

        private boolean available;

        private Builder() {
        }

        public Builder id(ProductID val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder money(Money val) {
            money = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
