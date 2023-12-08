package domain.valueObject;

import java.util.UUID;

public class ProductID extends BaseId<UUID>{

    public ProductID(UUID value) {
        super(value);
    }
}
