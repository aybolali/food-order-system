package com.food.ordering.system.order.service.dataAccess.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderItemEntityId.class) //creating a custom class to represent the composite primary key for your entity.
@Table(name = "order_items")
@Entity
public class OrderItemEntity {
    @Id
    private Long id;

    @Id //multi column primary key
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order; //for being unique of each item in a specific order

    private UUID productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemEntity that = (OrderItemEntity) o;

        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }

}
