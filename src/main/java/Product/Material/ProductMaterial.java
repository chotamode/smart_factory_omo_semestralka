package Product.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class ProductMaterial {

    private int quantityNeeded;
    private int quantityActual = 0;
    private MaterialUnits unit;
    private MaterialType type;
    private StockMaterial stockMaterial;

    public ProductMaterial(int quantityNeeded, MaterialUnits unit, MaterialType type, StockMaterial stockMaterial) {
        this.quantityNeeded = quantityNeeded;
        this.unit = unit;
        this.type = type;
        this.stockMaterial = stockMaterial;
    }

    public ProductMaterial(int quantityNeeded,
                           MaterialUnits unit,
                           MaterialType type) {
        this(quantityNeeded, unit, type, null);
    }

    public ProductMaterial(MaterialType type) {
        this(1, MaterialUnits.UNIT, type, null);
    }

    public ProductMaterial(int quantityNeeded, MaterialType type) {
        this(quantityNeeded, MaterialUnits.UNIT, type, null);
    }

    public Optional<StockMaterial> getStockMaterial() {
        return Optional.ofNullable(stockMaterial);
    }

    public int getCost() {
        return stockMaterial.getPrice() * quantityNeeded;
    }

    public void addMaterial() throws Exception {
        quantityActual = quantityNeeded;
        stockMaterial.use(quantityNeeded);
    }
}
