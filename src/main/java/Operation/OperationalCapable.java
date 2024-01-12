package Operation;

import EventManagement.Channels.ProductionEventChannel;
import Exceptions.ConditionException;
import Management.Visitable;
import Operation.WorkType.WorkType;
import Product.Product;

/**
 * Interface for the chain of responsibility pattern
 */
public interface OperationalCapable extends Visitable {
    void workOnProduct(Product product) throws ConditionException, Exception;
    void setNextWorker(OperationalCapable nextWorker);
    WorkType getWorkType();

    OperationalCapable getNextWorker();

    void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel);

    boolean isWorking();
    void setWorking(boolean working);

    void setLinePriority(int linePriority);
    int getLinePriority();

    int getTotalMaintenanceCost();
}
