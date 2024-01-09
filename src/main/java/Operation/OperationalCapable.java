package Operation;

import EventManagement.Channels.ProductionEventChannel;
import Operation.WorkType.WorkType;
import Product.Product;

/**
 * Interface for the chain of responsibility pattern
 */
public interface OperationalCapable {
    void workOnProduct(Product product);
    void setNextWorker(OperationalCapable nextWorker);
    WorkType getWorkType();

    OperationalCapable getNextWorker();

    void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel);
}
