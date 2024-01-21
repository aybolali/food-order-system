package com.food.ordering.system.common.dataAccess.restaurant.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEntityId implements Serializable {
    private UUID restaurantId;
    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantEntityId that = (RestaurantEntityId) o;

        if (!Objects.equals(restaurantId, that.restaurantId)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, restaurantId);
    }
}
