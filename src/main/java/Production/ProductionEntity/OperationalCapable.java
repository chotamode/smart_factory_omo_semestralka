package Production.ProductionEntity;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventListener.EventListener;
import EventManagement.EventPublisher.EventPublisher;
import Management.Visitable;
import Operation.WorkType.WorkType;
import Product.Product;
import Production.Configurable;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Worker;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for the chain of responsibility pattern.
 * OperationalCapable works on product and sends event that operation is done to the next worker.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Worker.class, name = "worker"),
        @JsonSubTypes.Type(value = Machine.class, name = "machine"),
        @JsonSubTypes.Type(value = Cobot.class, name = "cobot")
})
public interface OperationalCapable extends Visitable, Configurable, EventPublisher, EventListener {
    void workOnProduct(Product product) throws Exception;

    WorkType getWorkType();

    OperationalCapable getNextWorker();

    void setNextWorker(OperationalCapable nextWorker);

    void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel);

    boolean isWorking();

    void setWorking(boolean working);

    int getLinePriority();

    void setLinePriority(int linePriority);

    int getTotalMaintenanceCost();
}
