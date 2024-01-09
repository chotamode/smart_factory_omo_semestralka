package EventManagement.Events;

import Operation.OperationalCapable;
import Product.Product;
import ProductionEntity.ProductionEntity;
import lombok.Getter;

@Getter
public class ProductionEvent extends Event{

    Product product;
    OperationalCapable target;


    public ProductionEvent(String name, EventType type, Product product, OperationalCapable operationalCapable, Long timeStamp) {
        super(name, type, timeStamp);
        this.product = product;
        this.target = operationalCapable;
    }
}
