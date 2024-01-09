package Factory;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Events.EventType;
import EventManagement.Events.ProductionEvent;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import Product.ProductSeries;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Production line used to create products. Production line is different for each product.
 * It consists of devices and workers, each of which have link to next device/worker in the line.
 *
 */
@Getter
@Setter
public class ProductionLine implements TimeObserver {
    private OperationalCapable firstWorker;
    private OperationalCapable lastWorker;
    private OperationalCapable currentWorker;

    private ProductSeries productSeries;

    private List<WorkType> workerSequence;
    private boolean working = false;

    private int currentProductIndex = 0;

    private ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    private Logger logger = LogManager.getLogger(ProductionLine.class);

    public ProductionLine() {
        subscribeToTimeAndReportManager();
    }

    /**
     * Adds worker to the production line
     * @param worker Object of class implementing ProductionEntity interface
     */
    public void addWorker(OperationalCapable worker) {
        worker.subscribeToProductionEventChannel(productionEventChannel);
        if (firstWorker == null) {
            firstWorker = worker;
            lastWorker = worker;
            currentWorker = worker;
        } else {
            lastWorker.setNextWorker(worker);
            lastWorker = worker;
        }
    }

    /**
     * Assemble production line for given product series.
     * Finding the right first worker and then adding all the workers to the line by setting nextWorker.
     *
     * @param productSeries Product series to assemble production line for
     */
    public void assembleProductionLine(ProductSeries productSeries) {
        this.productSeries = productSeries;
        this.workerSequence = productSeries.getWorkerSequence();

        this.working = true;
    }

    /**
     * Every worker performs operation on product and passes it to the next worker if it's finished
     * by creating new event and putting it to the ProductionEventChannel.
     * If there is no next worker, product is finished.
     *
     */
    public void work() {
        if(currentProductIndex >= productSeries.getProducts().size()) {
            working = false;
            logger.info("Production line finished working");
            return;
        }
        if (firstWorker == null) {
            throw new RuntimeException("No workers in production line for product "
                    + productSeries.getProductName() +
                    " with index " +
                    currentProductIndex +
                    " at time " +
                    TimeAndReportManager.getInstance().getCurrentTime());
        }
        productionEventChannel.publishEvent(new ProductionEvent(
                "Product is ready for next operation",
                EventType.PRODUCT_READY_FOR_NEXT_OPERATION,
                productSeries.getProducts().get(currentProductIndex),
                firstWorker,
                TimeAndReportManager.getInstance().getCurrentTime()
        ));
//        firstWorker.workOnProduct(productSeries.getProducts().get(currentProductIndex));
        currentProductIndex++;
    }

    public void workUntilFinished() {
        working = true;
        logger.info("Production line started working");
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        if (working) {
            if(currentProductIndex >= productSeries.getProducts().size()) {
                logger.info("Production line finished working at time " + time);
                working = false;
                return false;
            }

            logger.info("Factory Line started work on product " + productSeries.getProducts().get(currentProductIndex).getName() + currentProductIndex + " at time " + time);

            work();
            return true;
        }
        return false;
    }
}
