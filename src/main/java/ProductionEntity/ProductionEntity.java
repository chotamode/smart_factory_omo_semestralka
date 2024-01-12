package ProductionEntity;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventPublisher.ProductionEventEventPublisher;
import EventManagement.Events.ProductionEvent;
import Operation.Operation;
import Operation.WorkType.WorkType;
import Operation.OperationalCapable;
import Product.Product;
import EventManagement.Events.EventType;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public abstract class ProductionEntity implements ProductionEventListener, ProductionEventEventPublisher, OperationalCapable {

    private static int lastId = 0;
    private final int id = lastId++;

    private int linePriority;

    private int hourlyMaintenanceCost = 1;
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
    public void react(ProductionEvent event) throws Exception {
        if (event.getType() == EventType.PRODUCT_READY_FOR_NEXT_OPERATION) {
            if (event.getTarget() == this) {
                workOnProduct(event.getProduct());
            }
        }
    }

    @Override
    public void workOnProduct(Product product) throws Exception {
        totalMaintenanceCost += hourlyMaintenanceCost;
        logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " " + this.getClass().getSimpleName() + this.getId() + " is working on product " + product.getName() + product.getSeriesIndex());
        Operation operation = product.getCurrentOperation();
        operation.perform();
        if(product.getNextOperation() == null) {
            product.setFinished(true);
            logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " Product " +
                    product.getName() + product.getSeriesIndex() +
                    " by " + this.getClass().getSimpleName() + this.getId() +
                    " finished");
            return;
        }
        if (operation.isFinished()) {
            totalMaintenanceCost += hourlyMaintenanceCost;
            product.setNextOperation();
            publishEvent(new ProductionEvent("Product is ready for next operation",
                    EventType.PRODUCT_READY_FOR_NEXT_OPERATION,
                    product,
                    nextWorker,
                    this,
                    TimeAndReportManager.getInstance().getCurrentTime()));
        }
    }

    public void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel) {
        productionEventChannel.subscribeAsListener(this);
        productionEventChannel.subscribeAsPublisher(this);
        this.productionEventChannel = productionEventChannel;
        productionEventChannel.getClass().getSimpleName();
    }


}
