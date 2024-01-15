package Product.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockMaterial {

    private static final UUID id = UUID.randomUUID();
    private int quantity;
    private MaterialUnits unit = MaterialUnits.UNIT;
    private MaterialType type;
    /**
     * Price per unit
     */
    private int price;

    public StockMaterial(int quantity, MaterialType type) {
        this.quantity = quantity;
        this.type = type;
        this.price = type.getPrice();
    }

    public void use(int quantity) throws Exception {
        if (quantity > this.quantity) {
            throw new Exception("Not enough material");
        }
        this.quantity -= quantity;
    }

    public void add(int amount) {
        quantity += amount;
    }
}
