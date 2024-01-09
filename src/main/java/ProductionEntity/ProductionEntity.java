package ProductionEntity;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.Events.ProductionEvent;
import Exceptions.DeviceResource.ConditionException;
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
public abstract class ProductionEntity implements ProductionEventListener, ProductionEventPublisher, OperationalCapable {

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
        logger.info(this.getClass().getSimpleName() + " is working on product " + product.getName() + product.getSeriesIndex() + " at time: " + TimeAndReportManager.getInstance().getCurrentTime());
        Operation operation = product.getCurrentOperation();
        operation.perform();
        if(product.getNextOperation() == null) {
            product.setFinished(true);
            logger.info("Product " +
                    product.getName() + product.getSeriesIndex() +
                    " finished at time: " +
                    TimeAndReportManager.getInstance().getCurrentTime() +
                    " by " + this.getClass().getSimpleName());
            return;
        }
        if (operation.isFinished()) {
            product.setNextOperation();
            publishEvent(new ProductionEvent("Product is ready for next operation",
                    EventType.PRODUCT_READY_FOR_NEXT_OPERATION,
                    product,
                    nextWorker, TimeAndReportManager.getInstance().getCurrentTime()));
        }
    }

    public void subscribeToProductionEventChannel(ProductionEventChannel productionEventChannel) {
        productionEventChannel.subscribeAsListener(this);
        productionEventChannel.subscribeAsPublisher(this);
        this.productionEventChannel = productionEventChannel;
    }


}
