package EventManagement.Events;

import EventManagement.EventPublisher.EventPublisher;
import Product.Product;
import Production.ProductionEntity.OperationalCapable;
import lombok.Getter;

@Getter
public class ProductionEvent extends Event {

    Product product;
    OperationalCapable target;


    public ProductionEvent(String name, EventType type, Product product, OperationalCapable target, EventPublisher source, Long timeStamp) {
        super(name, type, timeStamp, source, target);
        this.product = product;
        this.target = target;
    }
}
