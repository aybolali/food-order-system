package domain.valueObject;

import java.util.UUID;

public class RestaurantID extends BaseId<UUID> {
    public RestaurantID(UUID value) {
        super(value);
    }
}
