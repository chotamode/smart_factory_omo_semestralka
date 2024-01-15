package Production.ProductionEntity;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.Events.EventType;
import EventManagement.Events.ProductionEvent;
import Operation.Operation;
import Operation.WorkType.WorkType;
import Product.Product;
import Production.Configuration;
import Production.ProductionLine.ProductionLine;
import Util.Constants;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for production entities.
 * Production entities are devices and workers.
 * They are used to create products.
 * They have link to next production entity in the production line implementing pattern Chain of responsibility.
 */
@Getter
@Setter
public abstract class ProductionEntity implements ProductionEventListener, ProductionEventPublisher, OperationalCapable {

    private static int lastId = 0;
    private final int id = lastId++;

    private int linePriority;

    private int hourlyMaintenanceCost = Constants.DEFAULT_PRODUCTION_ENTITY_HOURLY_MAINTENANCE_COST;
    private int totalMaintenanceCost = 0;
    private int totalPaidMaintenanceCost = 0;

    private OperationalCapable nextWorker;
    private WorkType workType;
    private boolean working = false;

    private ProductionEventChannel productionEventChannel;

    private Logger logger = LogManager.getLogger(ProductionEntity.class);

    public ProductionEntity(WorkType workType) {
        this.workType = workType;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName() + this.getId();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + this.getId();
    }

    @Override
    public void react(ProductionEvent event) throws Exception {
        if (event.getType() == EventType.PRODUCT_READY_FOR_NEXT_OPERATION) {
            if (event.getTarget() == this) {
                workOnProduct(event.getProduct());
            }
        }
    }

    /**
     * Method for working on product.
     * It is called by {@link ProductionLine} when product is ready for next operation.
     * It calls {@link Operation#perform()} method on current operation of the product.
     * If current operation is finished, it calls {@link Product#setNextOperation()} method on the product.
     * If there is no next operation, it sets product as finished.
     *
     * @param product product to work on
     */
    @Override
    public void workOnProduct(Product product) throws Exception {
        totalMaintenanceCost += hourlyMaintenanceCost;
        logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " " + this.getClass().getSimpleName() + this.getId() + " is working on product " + product.getName() + product.getSeriesIndex());
        Operation operation = product.getCurrentOperation();
        if(operation.getWorkType() != this.workType) {
            throw new RuntimeException("Production entity " + this.getName() + " is working on operation " + operation.getWorkType() + " but it should be working on operation " + this.workType);
        }else{
            operation.perform();
        }
        if (product.getNextOperation() == null) {
            product.setFinished(true);
            logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " Product " +
                    product.getName() + product.getSeriesIndex() +
                    " by " + this.getClass().getSimpleName() + this.getId() +
                    " finished");
            return;
        }
        if (operation.isFinished() && product.getCurrentOperation() == operation) {
            totalMaintenanceCost += hourlyMaintenanceCost;
            product.setNextOperation();
//            logger.debug(product + " has been set to next operation by " + this.getName());
            publishEvent(new ProductionEvent("Product is ready for next operation",
                    EventType.PRODUCT_READY_FOR_NEXT_OPERATION,
                    product,
                    nextWorker,
                    this,
                    TimeAndReportManager.getInstance().getCurrentTime()));
        }
    }

    @Override
    public Configuration getConfigurations() {
        List<Map<String, String>> configurations = new ArrayList<>();

        configurations.add(Map.of("Name", this.getClass().getSimpleName() + this.getId()));
        configurations.add(Map.of("Hourly maintenance cost", String.valueOf(hourlyMaintenanceCost)));
        configurations.add(Map.of("Work type", workType.toString()));
        configurations.add(Map.of("Line priority", String.valueOf(linePriority)));

        return new Configuration(configurations, this);
    }

    public void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel) {
        productionEventChannel.subscribeAsListener(this);
        productionEventChannel.subscribeAsPublisher(this);
        this.productionEventChannel = productionEventChannel;
    }


}
