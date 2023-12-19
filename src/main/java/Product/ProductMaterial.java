package Product;

public record ProductMaterial(int quantity,
                              MaterialUnits unit,
                              MaterialType type,
                              StockMaterial stockMaterial) {
}
