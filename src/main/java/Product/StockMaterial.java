package Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockMaterial {

    private int quantity;
    private MaterialUnits unit;
    private MaterialType type;
    private static final UUID id = UUID.randomUUID();
    /**
     * Price per unit
     */
    private int price;

    public void use(int quantity) throws Exception {
        if (quantity > this.quantity) {
            throw new Exception("Not enough material");
        }
        this.quantity -= quantity;
    }

    public void restock(int quantity) {
        this.quantity += quantity;
    }
}
