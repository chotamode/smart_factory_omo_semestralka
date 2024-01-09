package Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductMaterial {

    private int quantity;
    private MaterialUnits unit;
    private MaterialType type;
    private StockMaterial stockMaterial;

    public ProductMaterial(int quantity,
                           MaterialUnits unit,
                           MaterialType type) {
        this(quantity, unit, type, null);
    }

    public ProductMaterial(MaterialType type) {
        this(1, MaterialUnits.UNIT, type, null);
    }

    public ProductMaterial(int quantity, MaterialType type) {
        this(quantity, MaterialUnits.UNIT, type, null);
    }

    public Optional<StockMaterial> getStockMaterial() {
        return Optional.ofNullable(stockMaterial);
    }
}
