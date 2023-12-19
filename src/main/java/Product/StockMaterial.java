package Product;

import java.util.UUID;

public record StockMaterial(int quantity,
                            MaterialUnits unit,
                            MaterialType type){
    private static final UUID id = UUID.randomUUID();
}
