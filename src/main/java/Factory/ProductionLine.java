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
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
    private ProductionLineState state = ProductionLineState.FREE;

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

        this.state = ProductionLineState.AWAITING_START;
    }

    /**
     * Every worker performs operation on product and passes it to the next worker if it's finished
     * by creating new event and putting it to the ProductionEventChannel.
     * If there is no next worker, product is finished.
     *
     */
    public void work() {
        if(currentProductIndex >= productSeries.getProducts().size()) {
            state = ProductionLineState.FREE;
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

    public void workUntilFinished() throws Exception {

        if(!isLineReady()) {
            throw new RuntimeException("Production line is not ready to start working");
        }

        state = ProductionLineState.WORKING;
        logger.info("Production line started working");
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        if (state == ProductionLineState.WORKING) {
            if(productSeries.isFinished()) {
                logger.info("Production line finished working at time " + time);
                state = ProductionLineState.FREE;
                for(OperationalCapable worker : getWorkers()) {
                    worker.setWorking(false);
                }
                return false;
            }

            if(currentProductIndex < productSeries.getProducts().size()){
                logger.info("Factory Line started work on product " + productSeries.getProducts().get(currentProductIndex).getName() + currentProductIndex + " at time " + time);

                work();
                return true;
            }
        }
        return false;
    }

    private List<OperationalCapable> getWorkers() {
        List<OperationalCapable> workers = new ArrayList<>();
        OperationalCapable worker = firstWorker;
        while(worker != null) {
            workers.add(worker);
            worker = worker.getNextWorker();
        }
        return workers;
    }

    /**
     * Checks if the production line is ready to start working on product series.
     * Checks if there is enough workers in the line and if they are capable of working on the product series.
     * @return
     */
    private boolean isLineReady() throws Exception {
        if(productSeries == null){
            throw new Exception("Production line is not assembled");
        }
        if(workerSequence.size() < productSeries.getWorkerSequence().size()){
            throw new Exception("Not enough workers in production line for product " + productSeries.getProductName());
        }
        for(int i = 0; i < productSeries.getWorkerSequence().size(); i++){
            if(!workerSequence.get(i).equals(productSeries.getWorkerSequence().get(i))){
                throw new Exception("Production line is not capable of working on product " + productSeries.getProductName());
            }
        }
        return true;
    }

    public List<OperationalCapable> getOperationalCapables() {
        List<OperationalCapable> operationalCapables = new ArrayList<>();
        OperationalCapable operationalCapable = firstWorker;
        while(operationalCapable != null) {
            operationalCapables.add(operationalCapable);
            operationalCapable = operationalCapable.getNextWorker();
        }
        return operationalCapables;
    }
}
